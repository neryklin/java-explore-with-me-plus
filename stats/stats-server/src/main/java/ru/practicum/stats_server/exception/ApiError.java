package ru.practicum.stats_server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ApiError {
    private HttpStatus status;
    private String description;
    private String message;
    private String stackTrace;
}
