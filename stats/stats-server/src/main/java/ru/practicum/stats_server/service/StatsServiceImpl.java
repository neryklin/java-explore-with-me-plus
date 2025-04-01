package ru.practicum.stats_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.ResponseHitDto;
import ru.practicum.stats_dto.ResponseStatsDto;
import ru.practicum.stats_server.mapper.MapperHit;
import ru.practicum.stats_server.model.Hit;
import ru.practicum.stats_server.repository.StatsRepository;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public ResponseHitDto create(CreateHitDto createHitDto) {
        Hit hit = statsRepository.save(MapperHit.toHit(createHitDto));
        return MapperHit.toResponseHitDto(hit);
    }

    @Override
    public Collection<ResponseStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        //заглушка для дальнейшей реализации
        return List.of(new ResponseStatsDto());
    }
}
