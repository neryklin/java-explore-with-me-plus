package ru.practicum.compilations.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.Collection;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class RequestCompilationCreate {
    Collection<Long> events;
    @NotNull
    Boolean pinned;
    @NotNull
    String title;
}
