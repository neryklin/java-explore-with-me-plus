package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.CompilationsPublicParam;
import ru.practicum.compilations.service.CompilationService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/compilations")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CompilationsController {
    private final CompilationService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CompilationsDto> getAll(@RequestParam(required = false) boolean pinned,
                                              @RequestParam(defaultValue = "0") long from,
                                              @RequestParam(defaultValue = "10") long size) {
        log.info("GET Поиск подборок событий по параметрам");
        return service.findCompilations(new CompilationsPublicParam(pinned, from, size));
    }

    @GetMapping(path = "{compId}")
    public CompilationsDto getById(@PathVariable long compId) {
        log.info("GET Поиск подборки событий по id: {}", compId);
        return service.getById(compId);
    }
}
