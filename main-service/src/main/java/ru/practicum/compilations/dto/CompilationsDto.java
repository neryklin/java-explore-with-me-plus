package ru.practicum.compilations.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Builder
public class CompilationsDto {
    Collection<EventShortDto> events;
    Long id;
    Boolean pinned;
    String title;
}
