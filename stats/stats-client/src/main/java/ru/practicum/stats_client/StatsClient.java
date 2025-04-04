package ru.practicum.stats_client;

import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.CreateStatsDto;
import ru.practicum.stats_dto.ResponseStatsDto;

import java.util.List;

public interface StatsClient {

    void sendHit(CreateHitDto hitDto);

    List<ResponseStatsDto> getStats(CreateStatsDto statsDto);
}
