package ru.practicum.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDto {

    private Long id;

    @NotBlank(message = "Название категории не может быть пустым")
    @Size(max = 50, message = "Название категории не должно превышать 50 символов")
    private String name;
}
