package ru.practicum.ewm.service.events.service;

import ru.practicum.ewm.service.stats.common.dto.StatResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface StatisticsService {
    List<StatResponse> getStatistics(LocalDateTime start,
                                     LocalDateTime end,
                                     List<String> uris,
                                     Boolean unique);

    void saveStatistics(String app, String uri, String ip, LocalDateTime timestamp);

    /**
     * Returns a Map of eventId to view count.
     */
    Map<Long, Long> getEventsViews(List<Long> eventIds,
                                   LocalDateTime start,
                                   LocalDateTime end,
                                   boolean unique);
}
