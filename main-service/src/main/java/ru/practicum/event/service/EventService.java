package ru.practicum.event.service;


import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;

import java.util.Collection;

@Transactional(readOnly = true)
public interface EventService {

    @Transactional
    EventFullDto create(Long userId, NewEventDto eventDto);

    EventFullDto getEventById(Long userId, Integer eventId);

    Collection<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size);

    @Transactional
    EventFullDto updateEvent(Long userId, Integer eventId, UpdateEventUserRequest updateEventUserRequest);


    List<EventFullDto> search(EventSearchParameters parameters);

    EventFullDto update(Long eventId, UpdateEventDto updateEventDto);

}
