package ru.practicum.compilations.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@Getter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class RequestCompilationUpdate {
    Collection<Long> events;
    Boolean pinned;
    String title;
}
