package ru.practicum.ewm.service.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static ru.practicum.ewm.service.error.ErrorConstants.*;

@Slf4j
@RestControllerAdvice
public class DefaultErrorHandler {

    @ExceptionHandler(EwmServiceException.class)
    public ResponseEntity<ApiError> handleEwmServiceException(final EwmServiceException ex) {
        log.error("Received ApiError with message: {}, status: {}, reason: {}, at time: {}",
                ex.getMessage(), ex.getStatus(), ex.getReason(), ex.getTimestamp());
        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .reason(ex.getReason())
                .status(ex.getStatus().toString())
                .timestamp(ex.getTimestamp())
                .build();
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInvalidParamsException(final MethodArgumentNotValidException ex) {
        log.error("Received MethodArgumentNotValidException with message: {}", ex.getMessage());
        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .reason(BAD_REQUEST_REASON)
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(final RuntimeException ex) {
        log.error("Received a RuntimeException with message: {} at time: {}",
                ex.getMessage(),
                LocalDateTime.now());
        ApiError error = ApiError.builder()
                .message(ex.getMessage())
                .reason(UNKNOWN_REASON)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
