package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorHandler2 {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException e) {
        log.error("NotFoundException with message {} was thrown", e.getMessage());
        return ErrorResponse.create(e, HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEventDateValidationException(final EventDateValidationException e) {
        log.error("EventDateValidationException with message {} was thrown", e.getMessage());
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflict(final ConflictException e) {
        log.error("409 {}", e.getMessage());
        return ErrorResponse.create(e, HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleConflict(final ForbiddenException e) {
        log.error("403 {}", e.getMessage());
        return ErrorResponse.create(e, HttpStatus.FORBIDDEN, e.getMessage());
    }
}