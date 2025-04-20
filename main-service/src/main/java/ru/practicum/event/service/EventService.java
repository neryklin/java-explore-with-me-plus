package ru.practicum.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.*;

import java.util.Collection;
import java.util.List;

@Transactional(readOnly = true)
public interface EventService {

    EventFullDto getEventById(long eventId);

    Collection<EventShortDto> getEventsByFilter(EventPublicParam param);


    @Transactional
    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto getEventById(Long userId, Integer eventId);

    Collection<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size);

    @Transactional
    EventFullDto updateEvent(Long userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest);


    List<EventFullDto> search(EventSearchParameters parameters);

    EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
