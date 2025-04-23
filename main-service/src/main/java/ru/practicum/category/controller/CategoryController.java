package ru.practicum.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return categoryService.findAll(from, size);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryService.findById(id);
    } // получаем категорию по id

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto dto) {
        return categoryService.create(dto);
    }

    @PatchMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @RequestBody @Valid CategoryDto dto) {
        return categoryService.update(id, dto);
    } // обновка категории по id

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
    } //удаляем категорию по id
}
