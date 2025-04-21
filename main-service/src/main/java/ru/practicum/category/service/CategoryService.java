package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll(Integer from, Integer size);

    CategoryDto findById(Long id);

    CategoryDto create(CategoryDto dto);

    CategoryDto update(Long id, CategoryDto dto);

    void delete(Long id);

    Category findCategoryById(Long id);
}
