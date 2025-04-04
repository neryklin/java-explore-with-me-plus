package ru.practicum.stats_dto;


import jakarta.validation.constraints.NotEmpty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateHitDto {
    long id;

    @NotEmpty
    @Size(max = 255)
    String app;

    @NotEmpty
    @Size(max = 255)
    String uri;

    @NotEmpty
    @Size(max = 255)
    String app;

    @Size(max = 255)
    String uri;

    @Size(max = 255)
    String ip;

    @NotNull
    LocalDateTime timestamp;


}
