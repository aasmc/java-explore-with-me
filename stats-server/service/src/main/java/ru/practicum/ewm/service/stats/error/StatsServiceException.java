package ru.practicum.ewm.service.stats.error;

import lombok.Getter;

public class StatsServiceException extends RuntimeException {

    @Getter
    private final int code;

    public StatsServiceException(int code, String message) {
        super(message);
        this.code = code;
    }
}
