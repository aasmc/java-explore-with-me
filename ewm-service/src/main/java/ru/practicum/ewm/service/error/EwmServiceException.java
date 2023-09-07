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

    public static EwmServiceException dataIntegrityException(String message) {
        return new EwmServiceException(
                message,
                ErrorConstants.DATA_INTEGRITY_VIOLATION_REASON,
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }

    public static EwmServiceException notFoundException(String message) {
        return new EwmServiceException(
                message,
                ErrorConstants.NOT_FOUND_REASON,
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    public static EwmServiceException incorrectParameters(String message) {
        return new EwmServiceException(
                message,
                ErrorConstants.BAD_REQUEST_REASON,
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

    public static EwmServiceException wrongConditions(String message) {
        return new EwmServiceException(
                message,
                ErrorConstants.CONDITIONS_NOT_MET_REASON,
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }
}
