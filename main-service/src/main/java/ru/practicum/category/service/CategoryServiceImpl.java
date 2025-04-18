package ru.practicum.category.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.MapperCategory;
import ru.practicum.category.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(MapperCategory::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория с id " + id + " не найдена"));
        return MapperCategory.toCategoryDto(category);
    }

    @Override
    public CategoryDto create(CategoryDto dto) {
        Category category = MapperCategory.toCategory(dto);
        return MapperCategory.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDto update(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория с id " + id + " не найдена"));
        category.setName(dto.getName());
        return MapperCategory.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Категория с id " + id + " не найдена");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория с id " + id + " не найдена"));
    }
}
