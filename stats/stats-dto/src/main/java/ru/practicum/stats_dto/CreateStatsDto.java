package ru.practicum.stats_dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateStatsDto {

    @NotNull
    String start;

    @NotNull
    String end;

    List<String> uris = new ArrayList<>();

    Boolean unique;
}