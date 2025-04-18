package ru.practicum.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.RequestUserCreate;
import ru.practicum.user.dto.RequestUserUpdate;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserParam;
import ru.practicum.user.service.UserService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/admin/users")
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@RequestBody @Valid RequestUserCreate body) {
        log.info("POST Создание пользователя {}", body);
        UserParam params = new UserParam(body.getName(), body.getEmail(), null, null, null, null);
        return userService.createUser(params);
    }

    @PutMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto updateUser(@RequestBody @Valid RequestUserUpdate body,
                              @PathVariable long userId) {
        log.info("PUT Обновление пользователя {}", body);
        UserParam params = new UserParam(body.getName(), body.getEmail(), userId, null, null, null);
        return userService.updateUser(params);
    }

    @GetMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUser(@PathVariable long userId) {
        log.info("GET Поиск пользователя по id: {}", userId);
        UserParam params = new UserParam(null, null, userId, null, null, null);
        return userService.findUserById(params);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<UserDto> getUsersByIds(@RequestParam(required = false) Collection<Long> ids,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET Поиск пользователей по списку id: {}", ids);
        UserParam params = new UserParam(null, null, null, ids, size, from);
        return userService.findUsersById(params);
    }

    @GetMapping(path = "/email")
    @ResponseStatus(HttpStatus.OK)
    public UserDto findUserByEmail(@RequestParam String email) {
        log.info("GET Поиск пользователей по Email: {}", email);
        UserParam params = new UserParam(null, email, null, null, null, null);
        return userService.findUserByEmail(params);
    }

    @DeleteMapping(path = "/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final long userId) {
        log.info("DELETE Удаление пользователя по id: {}", userId);
        UserParam params = new UserParam(null, null, userId, null, null, null);
        userService.deleteUser(params);
    }

}
