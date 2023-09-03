package ru.practicum.ewm.service.stats.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class DefaultErrorHandler {

    @ExceptionHandler(StatsServiceException.class)
    public ResponseEntity<ErrorResponse> handleStatsServiceException(StatsServiceException ex) {
        log.error("Received StatsServiceException. Message: {}. Code: {}", ex.getMessage(), ex.getCode());
        ErrorResponse response = ErrorResponse.builder()
                .code(ex.getCode())
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error("Unknown error. Message: {}", ex.getMessage());
        ErrorResponse response = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(ex.getMessage())
                .build();
        return ResponseEntity.internalServerError().body(response);
    }

}
