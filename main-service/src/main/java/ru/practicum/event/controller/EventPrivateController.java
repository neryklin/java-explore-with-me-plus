package ru.practicum.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Creating event by user with id {} - Start", userId);
        return eventService.create(userId, newEventDto);
    }

    @GetMapping
    public Collection<EventShortDto> getEventsByUser(@PathVariable Long userId,
                                                     @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                     @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Getting events for user id {} - Start", userId);
        return eventService.getEventsByUser(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long userId,
                                     @PathVariable Integer eventId) {
        log.info("Getting event id {} by user id {} - Start", eventId, userId);
        return eventService.getEventById(userId, eventId);

    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable Long userId,
                                          @PathVariable Integer eventId,
                                          @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("Updating event id {} by user id {} - Start", eventId, userId);
        return eventService.updateEvent(userId, eventId, updateEventUserRequest);
    }
}