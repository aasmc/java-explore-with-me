package ru.practicum.ewm.service.events.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.service.categories.domain.Category;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class EventShort {
    private final Long id;
    private final String annotation;
    private final Category category;
    private final LocalDateTime eventDate;
    private final Long initiatorId;
    private final String initiatorName;
    private final boolean paid;
    private final String title;
    private final int participationLimit;
    private final LocalDateTime publishDate;
}
