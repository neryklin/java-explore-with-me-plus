package ru.practicum.user.exception_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.user.exception.NotFoundUserException;

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
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка на сервере", e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError validError(final MethodArgumentNotValidException e) {
        log.debug("400 {}", e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        String field = null;
        String rejectedValue = null;

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            field = fieldError.getField();
            if (Objects.nonNull(fieldError.getRejectedValue())) {
                rejectedValue = fieldError.getRejectedValue().toString();
            }
        }

        String reason = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(e.getLocalizedMessage());

        String message = "field: " + field + " " + reason + " rejected value: " + rejectedValue;
        return new ApiError(HttpStatus.BAD_REQUEST, "Incorrectly made request.", message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError emailAlreadyExist(final DataIntegrityViolationException e) {
        String message = e.getLocalizedMessage();
        return new ApiError(HttpStatus.CONFLICT, "Integrity constraint has been violated.", message, LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundUser(final NotFoundUserException e) {
        return new ApiError(HttpStatus.NOT_FOUND, e.getReason(), e.getMessage(), LocalDateTime.now());
    }
}
