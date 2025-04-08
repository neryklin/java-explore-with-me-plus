package ru.practicum.user.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;


@Component
public class MapperUser {
    public static User toUser(UserDto userDto) {
        return User.builder()
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .build();
    }

    public static UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .build();
    }
}