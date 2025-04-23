package ru.practicum.event.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventPublicParam;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;
import ru.practicum.stats_client.StatsClient;
import ru.practicum.stats_dto.CreateHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
public class EventPublicController {
    private final StatsClient statsClient;
    private final EventService eventService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> findEvents(@RequestParam(required = false) String text,
                                                @RequestParam(required = false) Collection<Long> categories,
                                                @RequestParam(required = false) Boolean paid,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                @RequestParam(required = false)
                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @Future LocalDateTime rangeEnd,
                                                @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                @RequestParam(required = false) String sort,
                                                @RequestParam(defaultValue = "0") Long from,
                                                @RequestParam(defaultValue = "10") Long size,
                                                HttpServletRequest request) {

        EventPublicParam param = new EventPublicParam(text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        log.info("GET Поиск события по параметрам {}", param);
        CreateHitDto hitDto = CreateHitDto.builder()
                .app("ewm-main-service")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();

        statsClient.sendHit(hitDto);
        return eventService.getEventsByFilter(param);
    }

    @GetMapping(path = "/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getEventById(@PathVariable long eventId, HttpServletRequest request) {
        log.info("GET Поиск события по id {}", eventId);
        CreateHitDto hitDto = CreateHitDto.builder()
                .app("ewm-main-service")
                .ip(request.getRemoteAddr())
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
        boolean checkIp = Boolean.TRUE.equals(statsClient.checkIp(request.getRemoteAddr(), request.getRequestURI()).block());
        if (!checkIp) {
            eventService.changeViews(eventId);
        }

        statsClient.sendHit(hitDto);
        return eventService.getEventById(eventId);
    }
}
