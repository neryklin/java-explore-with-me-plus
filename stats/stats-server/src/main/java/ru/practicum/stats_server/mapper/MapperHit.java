package ru.practicum.stats_server.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.ResponseHitDto;
import ru.practicum.stats_server.model.Hit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperHit {

    public static Hit toHit(CreateHitDto createHitDto) {
        return Hit.builder()
                .id(createHitDto.getId())
                .uri(createHitDto.getUri())
                .ip(createHitDto.getIp())
                .timestamp(createHitDto.getTimestamp())
                .build();
    }

    public static ResponseHitDto toResponseHitDto(Hit hit) {
        return ResponseHitDto.builder()
                .id(hit.getId())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }
}
