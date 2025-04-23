package ru.practicum.stats_server.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.ResponseHitDto;
import ru.practicum.stats_dto.ResponseStatsDto;
import ru.practicum.stats_server.service.StatsService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ResponseStatsDto> getStats(@RequestParam String start,
                                                 @RequestParam String end,
                                                 @RequestParam(required = false) List<String> uris,
                                                 @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("GET запрос статистики  start= {}, end = {}, uris = {}, unique = {}",
                start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseHitDto createHit(@RequestBody @Valid CreateHitDto createHitDto) {
        log.info("POST Создаем hit {}", createHitDto);
        return statsService.create(createHitDto);
    }

    @GetMapping(path = "/stats/check")
    public boolean checkIp(@RequestParam String ip, @RequestParam String uri) {
        return statsService.checkIp(ip, uri);
    }
}
