package ru.practicum.ewm.service.util;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.error.ErrorConstants;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.EventShort;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class DateHelper {

    private final DateUtil dateUtil;

    public LocalDateTime toDate(String dateStr) {
        return dateUtil.toDate(dateStr);
    }

    public void checkNewEventDate(LocalDateTime eventDate) {
        LocalDateTime thresholdDate = LocalDateTime.now().plusHours(2);
        if (eventDate.isBefore(thresholdDate)) {
            String msg = String.format(ErrorConstants.EVENT_NEW_DATE_WRONG_MSG, eventDate);
            throw EwmServiceException.incorrectParameters(msg);
        }
    }

    public void checkDates(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && (start.isAfter(LocalDateTime.now()) || start.isAfter(end))) {
            String msg = String.format("Invalid date parameters: %s, %s",
                    start.format(DateUtil.FORMATTER),
                    end.format(DateUtil.FORMATTER));
            throw EwmServiceException.incorrectParameters(msg);
        }
    }

    public LocalDateTime getStartDateOrComputeIfNull(LocalDateTime start, List<Event> events) {
        if (start == null) {

            return events.stream()
                    .map(Event::getPublishedOn)
                    .filter(Objects::nonNull)
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
                    .filter(Objects::nonNull)
                    .min(Comparator.naturalOrder())
                    .orElse(dateUtil.getDefaultDate());
        }
        return start;
    }

}
