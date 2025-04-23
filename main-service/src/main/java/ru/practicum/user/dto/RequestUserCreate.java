package ru.practicum.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class RequestUserCreate {
    @Email
    @NotNull
    @Size(min = 6, max = 254)
    @Pattern(regexp = ".+@.+\\..{2,63}")
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
