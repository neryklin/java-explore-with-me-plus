package ru.practicum.stats_server.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.ResponseHitDto;
import ru.practicum.stats_dto.ResponseStatsDto;
import ru.practicum.stats_server.mapper.MapperHit;
import ru.practicum.stats_server.model.Hit;
import ru.practicum.stats_server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    @Transactional
    public ResponseHitDto create(CreateHitDto createHitDto) {
        Hit mapHit = MapperHit.toHit(createHitDto);
        Hit hit = statsRepository.save(mapHit);
        return MapperHit.toResponseHitDto(hit);
    }

    @Override
    public Collection<ResponseStatsDto> getStats(String start, String end, List<String> uris, Boolean unique) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(start,formatter);
        LocalDateTime endTime = LocalDateTime.parse(end,formatter);
        if (unique) {

            Collection<Object[]> response = statsRepository.findUniqueHit(startTime,endTime, uris);
            return response.stream().map(hit ->
                    ResponseStatsDto.builder()
                            .app(hit[0].toString())
                            .uri(hit[1].toString())
                            .hits((Long) hit[2]).build()
            ).toList();
        } else {
            Collection<Object[]> response = statsRepository.findAllHit(startTime,endTime, uris);
            return response.stream().map(hit ->
                    ResponseStatsDto.builder()
                            .app(hit[0].toString())
                            .uri(hit[1].toString())
                            .hits((Long) hit[2]).build()
            ).toList();
        }
    }
}
