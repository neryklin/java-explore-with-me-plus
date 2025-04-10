package ru.practicum.user.exception;

import lombok.Getter;

@Getter
public class NotFoundUserException extends RuntimeException {
    private final String reason;

    public NotFoundUserException(String reason) {
        super();
        this.reason = reason;
    }
}
