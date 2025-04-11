package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserUpdate {
    @Email
    @Size(min = 6, max = 254)
    @Pattern(regexp = ".+@.+\\..{2,63}")
    private String email;
    @Size(min = 2, max = 250)
    private String name;
}
