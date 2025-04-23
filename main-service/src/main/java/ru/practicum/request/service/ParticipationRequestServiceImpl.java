package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception_handler.ConflictException;
import ru.practicum.exception_handler.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.mapper.ParticipationRequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        User user = getUserOrThrow(userId);
        return requestRepository.findAllByRequester(user).stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = getUserOrThrow(userId);
        Event event = getEventOrThrow(eventId);

        if (!requestRepository.findByEventIdAndRequesterId(eventId, userId).isEmpty()) {
            throw new ConflictException("Request already exists");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new IllegalStateException("Нельзя подавать заявку на своё собственное событие.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new IllegalStateException("Нельзя подавать заявку на неопубликованное событие.");
        }


        List<ParticipationRequest> requests = requestRepository.findAllByEvent(event);

        if (!event.getRequestModeration() && requests.size() >= event.getParticipantLimit()) {
            throw new ConflictException("Member limit exceeded ");
        }

        ParticipationRequest request = ParticipationRequest.builder()
                .requester(user)
                .event(event)
                .status(RequestStatus.PENDING)
                .created(LocalDateTime.now())
                .build();
        if (event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED);
        }
        ParticipationRequestDto rez = ParticipationRequestMapper.toDto(requestRepository.save(request));
        return rez;
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Заявка не найдена."));

        if (!request.getRequester().getId().equals(userId)) {
            throw new IllegalStateException("Вы не можете отменить чужую заявку.");
        }

        request.setStatus(RequestStatus.CANCELED);
        return ParticipationRequestMapper.toDto(request);
    }

    private void throwDuplicateRequest() {
        throw new IllegalStateException("Заявка уже есть.");
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
    }

    private Event getEventOrThrow(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Событие не найдено."));
    }
}
