package ru.practicum.compilations.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.RequestCompilationCreate;
import ru.practicum.compilations.dto.RequestCompilationUpdate;
import ru.practicum.compilations.service.CompilationService;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationsAdminController {
    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationsDto post(@Valid @RequestBody RequestCompilationCreate create) {
        log.info("POST Создание подборки событий {}", create);
        return service.create(create);
    }

    @DeleteMapping(path = "{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        log.info("DELETE Удаление подборки событий compId: {}", compId);
        service.delete(compId);
    }

    @PatchMapping(path = "{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationsDto path(@PathVariable long compId, @Valid @RequestBody RequestCompilationUpdate update) {
        log.info("PATCH Обновление подборки событий {}", update);
        return service.update(compId, update);
    }


}
