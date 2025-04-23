package ru.practicum.compilations.model;

import lombok.RequiredArgsConstructor;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.event.model.MapperEvent;

@RequiredArgsConstructor
public class MapperCompilation {

    public static CompilationsDto toDto(Compilation compilation) {
        return CompilationsDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents()
                        .stream()
                        .map(MapperEvent::toEventShortDto)
                        .toList())
                .build();
    }
}
