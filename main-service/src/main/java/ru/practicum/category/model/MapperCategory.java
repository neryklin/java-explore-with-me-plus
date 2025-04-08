package ru.practicum.category.model;


import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;


@Component
public class MapperCategory {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .build();
    }
}
