package ru.practicum.category.model;


import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryDto;


@UtilityClass
public class MapperCategory {
    public static CategoryDto toCategoryDto(Category category) {
        if (category == null) return null;
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(CategoryDto dto) {
        if (dto == null) return null;
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
