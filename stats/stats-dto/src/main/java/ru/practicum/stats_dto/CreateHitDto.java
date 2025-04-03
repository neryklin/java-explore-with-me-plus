package ru.practicum.stats_dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateHitDto {
    long id;

    @Size(max = 255)
    String app;

    @Size(max = 255)
    String uri;

    @Size(max = 255)
    String ip;

    @NotNull
    String timestamp;

}
