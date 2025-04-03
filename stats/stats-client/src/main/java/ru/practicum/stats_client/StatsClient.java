package ru.practicum.stats_client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.CreateStatsDto;
import ru.practicum.stats_dto.ResponseStatsDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsClient {

    private final RestTemplate restTemplate;

    @Value("${stats-server.url:http://localhost:9090}")
    private String baseUrl;

    public void sendHit(CreateHitDto hitDto) {
        restTemplate.postForEntity(baseUrl + "/hit", hitDto, Void.class);
    }

    public List<ResponseStatsDto> getStats(CreateStatsDto statsDto) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(baseUrl + "/stats")
                .queryParam("start", statsDto.getStart())
                .queryParam("end", statsDto.getEnd());

        if (statsDto.getUris() != null) {
            for (String uri : statsDto.getUris()) {
                builder.queryParam("uris", uri);
            }
        }

        if (Boolean.TRUE.equals(statsDto.getUnique())) {
            builder.queryParam("unique", true);
        }

        ResponseEntity<ResponseStatsDto[]> response = restTemplate.getForEntity(
                builder.toUriString(), ResponseStatsDto[].class);

        return List.of(response.getBody());
    }
}