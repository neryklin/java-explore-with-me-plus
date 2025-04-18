package ru.practicum.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid CategoryDto newCategoryDto) {
        log.info("Пришел POST запрос /admin/categories с телом {}", newCategoryDto);
        final CategoryDto createdCategory = categoryService.create(newCategoryDto);
        return createdCategory;
    }

    @PatchMapping("/{categoryId}")
    public CategoryDto update(@PathVariable Long categoryId, @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Пришел PATCH запрос /admin/categories/{} с телом {}", categoryId, categoryDto);
        final CategoryDto updatedCategory = categoryService.update(categoryId, categoryDto);
        return updatedCategory;
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long categoryId) {
        log.info("Пришел DELETE запрос /admin/categories/{}", categoryId);
        categoryService.delete(categoryId);
    }
}
