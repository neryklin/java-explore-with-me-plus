package ru.practicum.event.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventSearchParameters;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> get(@RequestParam(required = false) List<Long> users,
                                  @RequestParam(required = false) List<String> states,
                                  @RequestParam(required = false) List<Long> categories,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                  @RequestParam(required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                  @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        log.info("Запрос списка событий: usersId {}, states {}, categoriesOd {}, rangeStart {}," +
                        " rangeEnd {}, from {}, size {}", users, states, categories, rangeStart,
                rangeEnd, from, size);

        List<EventState> statesValues = null;
        if (states != null) {
            statesValues = states.stream()
                    .map(EventState::valueOf)
                    .toList();
        }

        EventSearchParameters parameters = new EventSearchParameters(users, statesValues, categories, rangeStart,
                rangeEnd, from, size);
        return eventService.search(parameters);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable long eventId,
                               @RequestBody UpdateEventDto updateEventDto) {
        log.info("Запрос на изменения события {} с параметрами {}", eventId, updateEventDto);
        return eventService.update(eventId, updateEventDto);
    }

}
