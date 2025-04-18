package ru.practicum.user.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserParam;
import ru.practicum.user.model.MapperUser;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.Collection;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(UserParam params) {
        User user = new User();
        user.setName(params.name());
        user.setEmail(params.email());
        user = userRepository.save(user);
        return MapperUser.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserParam params) {
        User user = new User();
        user.setId(params.userId());
        if (Objects.nonNull(params.name())) {
            user.setName(params.name());
        }
        if (Objects.nonNull(params.email())) {
            user.setEmail(params.email());
        }
        user = userRepository.save(user);
        return MapperUser.toUserDto(user);
    }

    @Override
    public UserDto findUserById(UserParam params) {
        User user = userRepository.findById(params.userId())
                .orElseThrow(() -> new EntityNotFoundException("пользователь не найден по id"));
        return MapperUser.toUserDto(user);
    }

    @Override
    public Collection<UserDto> findUsersById(UserParam params) {
        Pageable pageable = PageRequest.of(params.from() > 0 ? params.from() / params.size() : 0, params.size());
        Page<User> users;
        if (Objects.nonNull(params.userIds())) {
            users = userRepository.findByIdIn(params.userIds(), pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return users
                .stream()
                .map(MapperUser::toUserDto)
                .toList();
    }

    @Override
    public UserDto findUserByEmail(UserParam params) {
        User user = userRepository.findByEmail(params.email())
                .orElseThrow(() -> new EntityNotFoundException("пользователь не найден по Email"));
        return MapperUser.toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(UserParam params) {
        userRepository.deleteById(params.userId());
    }
}
