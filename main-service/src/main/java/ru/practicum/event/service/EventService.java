package ru.practicum.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.*;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.Collection;
import java.util.List;


public interface EventService {

    EventFullDto getEventById(long eventId);

    Collection<EventShortDto> getEventsByFilter(EventPublicParam param);

    void changeViews(Long id);

    @Transactional
    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto getEventById(Long userId, Integer eventId);

    Collection<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size);

    @Transactional
    EventFullDto updateEvent(Long userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest);


    List<EventFullDto> search(EventSearchParameters parameters);

    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<ParticipationRequestDto> findRequestsByEventId(long userId, long eventId);

    EventRequestStatusUpdateResult updateRequestsStatus(long userId,
                                                        long eventId,
                                                        EventRequestStatusUpdateRequest request);


    Collection<ParticipationRequestDto> findAllRequestsByEventId(long userId, long eventId);
}
