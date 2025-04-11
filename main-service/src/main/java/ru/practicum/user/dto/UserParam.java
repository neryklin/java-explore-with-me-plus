package ru.practicum.user.dto;

import java.util.Collection;

public record UserParam(String name,
                        String email,
                        Long userId,
                        Collection<Long> userIds,
                        Integer size, Integer from) {
}