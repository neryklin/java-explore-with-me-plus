package ru.practicum.stats_server.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.ResponseHitDto;
import ru.practicum.stats_server.model.Hit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperHit {

    public static Hit toHit(CreateHitDto createHitDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Hit.builder()
                .id(createHitDto.getId())
                .app(createHitDto.getApp())
                .uri(createHitDto.getUri())
                .ip(createHitDto.getIp())
                .timestamp(LocalDateTime.parse(createHitDto.getTimestamp(), formatter))
                .build();
    }

    public static ResponseHitDto toResponseHitDto(Hit hit) {
        return ResponseHitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }
}
