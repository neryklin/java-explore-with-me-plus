package ru.practicum.event.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.enums.StateActionUser;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.MapperEvent;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.EventDateValidationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Slf4j
@Service("eventServiceImpl")
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final EventRepository eventRepository;//  private final
    private final RestTemplate restTemplate = new RestTemplate();


    @Override
    public EventFullDto create(Long userId, NewEventDto newEventDto) {
        if (newEventDto.getEventDate() != null && !newEventDto.getEventDate().isAfter(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            throw new EventDateValidationException("Event date 2+ hours after now");
        }
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category was not found " + newEventDto.getCategory()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with was not found " + userId));
        Event event = MapperEvent.toEvent(newEventDto, category, user);
        event.setLocation(locationRepository.save(newEventDto.getLocation()));

        if (newEventDto.getPaid() == null) {
            event.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            event.setParticipantLimit(0L);
        }
        if (newEventDto.getRequestModeration() == null) {
            event.setRequestModeration(true);
        }
        return MapperEvent.toEventFullDto(eventRepository.save(event));

    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        return eventRepository.findAllByInitiatorId(userId, page).stream()
                .map(MapperEvent::toEventShortDto).toList();
    }


    @Override
    public EventFullDto getEventById(Long userId, Integer eventId) {
        return MapperEvent.toEventFullDto(eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId)));
    }


    @Override
    public EventFullDto updateEvent(Long userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));

        validateForPrivate(event.getState(), updateEventUserRequest.getStateAction());

        Category category = categoryRepository.findById(updateEventUserRequest.getCategory())
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));

        Location location = locationRepository.save(updateEventUserRequest.getLocation());

        if (updateEventUserRequest.getEventDate() != null && !updateEventUserRequest.getEventDate().isAfter(LocalDateTime.now().plus(2, ChronoUnit.HOURS))) {
            throw new EventDateValidationException("Event date 2+ hours after now");
        }

        MapperEvent.updateFromDto(event, updateEventUserRequest, category, location);
        return MapperEvent.toEventFullDto(eventRepository.save(event));
    }

    private void validateForPrivate(EventState eventState, StateActionUser stateActionUser) {
        if (eventState.equals(EventState.PUBLISHED)) {
            throw new ConflictException("Can't change event not cancelled or in moderation");
        }
    }

}
