package ru.practicum.stats_dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseHitDto {
    long id;

    String app;

    String uri;

    String ip;

    LocalDateTime timestamp;

}
