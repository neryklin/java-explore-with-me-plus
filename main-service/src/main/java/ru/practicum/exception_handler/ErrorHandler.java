package ru.practicum.exception_handler;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exception.InvalidDateException;
import ru.practicum.exception.InvalidStateException;
import ru.practicum.exception_handler.dto.ApiError;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError serverError(final Exception e) {
        log.debug("500 {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        log.warn(stackTrace);
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), stackTrace, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError validError(final MethodArgumentNotValidException e) {
        log.debug("400 {}", e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        String field = null;
        String rejectedValue = null;
        String reason = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            field = fieldError.getField();
            if (Objects.nonNull(fieldError.getRejectedValue())) {
                rejectedValue = fieldError.getRejectedValue().toString();
            }
        }

        String message = "field: " + field + " " + reason + " rejected value: " + rejectedValue;
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request.", message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError emailAlreadyExist(final DataIntegrityViolationException e) {
        log.debug("409 {}", e.getMessage(), e);
        String message = e.getLocalizedMessage();
        return new ApiError(HttpStatus.CONFLICT, "Integrity constraint has been violated.", message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundExceptionHandler(final EntityNotFoundException e) {
        log.debug("404 {}", e.getMessage(), e);
        return new ApiError(HttpStatus.NOT_FOUND, "Запись не найдена", e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError invalidDateExceptionHandler(final InvalidDateException e) {
        log.debug("409 {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, "Ошибка в дате", e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError invalidStateExceptionHandler(final InvalidStateException e) {
        log.debug("409 {}", e.getMessage());
        return new ApiError(HttpStatus.BAD_REQUEST, "Ошибка в статусе", e.getMessage(), LocalDateTime.now());
    }

}
