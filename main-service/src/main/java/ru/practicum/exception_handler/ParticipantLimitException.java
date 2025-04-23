package ru.practicum.exception_handler;

public class ParticipantLimitException extends RuntimeException {
    public ParticipantLimitException(String message) {
        super(message);
    }
}
