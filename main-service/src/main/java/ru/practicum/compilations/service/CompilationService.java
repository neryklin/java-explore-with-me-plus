package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.CompilationsPublicParam;
import ru.practicum.compilations.dto.RequestCompilationCreate;
import ru.practicum.compilations.dto.RequestCompilationUpdate;

import java.util.Collection;

public interface CompilationService {
    CompilationsDto create(RequestCompilationCreate create);

    CompilationsDto update(long compId, RequestCompilationUpdate update);

    void delete(long compId);

    CompilationsDto getById(long compId);

    Collection<CompilationsDto> findCompilations(CompilationsPublicParam param);
}
