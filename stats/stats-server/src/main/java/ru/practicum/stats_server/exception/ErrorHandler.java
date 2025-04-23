package ru.practicum.stats_server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(final Exception e) {
        log.debug("500 {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, "Ошибка на сервере", e.getMessage(), stackTrace);
    }

    @ExceptionHandler
    public ErrorResponse validationHandler(final ValidationException e) {
        log.error(e.getMessage());
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingSe(final MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException with message {} was thrown", e.getMessage());
        return ErrorResponse.create(e, HttpStatus.BAD_REQUEST, e.getMessage());
    }

}
