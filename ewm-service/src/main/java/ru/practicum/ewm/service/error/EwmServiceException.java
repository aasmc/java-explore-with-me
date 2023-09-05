package ru.practicum.ewm.service.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class EwmServiceException extends RuntimeException {

    private final String reason;
    private final HttpStatus status;
    private final LocalDateTime timestamp;

    public EwmServiceException(String message,
                               String reason,
                               HttpStatus status,
                               LocalDateTime timestamp) {
        super(message);
        this.reason = reason;
        this.status = status;
        this.timestamp = timestamp;
    }
}
