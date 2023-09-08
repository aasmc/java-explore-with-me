package ru.practicum.ewm.service.events.util;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.EventShort;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DateHelper {

    private final DateUtil dateUtil;

    public LocalDateTime getStartDateOrComputeIfNull(LocalDateTime start, List<Event> events) {
        if (start == null) {

            return events.stream()
                    .map(Event::getPublishedOn)
                    .min(Comparator.naturalOrder())
                    .orElse(dateUtil.getDefaultDate());
        }
        return start;
    }

    public LocalDateTime getEndDateOrComputeIfNull(LocalDateTime end) {
        return end == null ? dateUtil.getDefaultDate() : end;
    }

    public LocalDateTime getEventShortStartDateOrComputeIfNull(LocalDateTime start, List<EventShort> events) {
        if (start == null) {

            return events.stream()
                    .map(EventShort::getPublishDate)
                    .min(Comparator.naturalOrder())
                    .orElse(dateUtil.getDefaultDate());
        }
        return start;
    }

}
