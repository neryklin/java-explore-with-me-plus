package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchParameters;
import ru.practicum.event.dto.UpdateEventDto;

import java.util.List;

public interface EventService {
    List<EventFullDto> search(EventSearchParameters parameters);

    EventFullDto update(Long eventId, UpdateEventDto updateEventDto);
}
