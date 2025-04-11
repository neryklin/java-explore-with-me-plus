package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserParam;

import java.util.Collection;

public interface UserService {
    UserDto createUser(UserParam params);

    UserDto updateUser(UserParam params);

    UserDto findUserById(UserParam params);

    Collection<UserDto> findUsersById(UserParam params);

    UserDto findUserByEmail(UserParam params);

    void deleteUser(UserParam params);
}
