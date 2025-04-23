package ru.practicum.stats_server.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.ResponseHitDto;
import ru.practicum.stats_dto.ResponseStatsDto;

import java.util.Collection;
import java.util.List;

public interface StatsService {

    @Transactional
    ResponseHitDto create(CreateHitDto createHitDto);

    @Transactional(readOnly = true)
    Collection<ResponseStatsDto> getStats(String start, String end, List<String> uris, Boolean unique);

    boolean checkIp(String ip, String uri);

}
