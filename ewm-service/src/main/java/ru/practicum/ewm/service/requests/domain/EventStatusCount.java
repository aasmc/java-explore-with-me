package ru.practicum.ewm.service.requests.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class EventStatusCount {
    private final Long eventId;
    private final Long count;
}
