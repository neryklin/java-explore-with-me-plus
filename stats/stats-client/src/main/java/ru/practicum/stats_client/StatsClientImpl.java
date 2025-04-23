package ru.practicum.stats_client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import ru.practicum.stats_dto.CreateHitDto;
import ru.practicum.stats_dto.CreateStatsDto;
import ru.practicum.stats_dto.ResponseStatsDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StatsClientImpl implements StatsClient {

    private final WebClient webClient;

    @Override
    public void sendHit(CreateHitDto hitDto) {
        webClient.post()
                .uri("/hit")
                .bodyValue(hitDto)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Override
    public List<ResponseStatsDto> getStats(CreateStatsDto statsDto) {
        return webClient.get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder
                            .path("/stats")
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

                    return builder.build();
                })
                .retrieve()
                .bodyToFlux(ResponseStatsDto.class)
                .collectList()
                .block();
    }


    public Mono<Boolean> checkIp(String ip, String uri) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/stats/check")
                        .queryParam("ip", ip)
                        .queryParam("uri", uri)
                        .build()
                ).retrieve()
                .bodyToMono(Boolean.class);
    }
}