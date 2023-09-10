package ru.practicum.ewm.service.events.service;

import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.mapper.EventMapper;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public abstract class BaseEventService {

    private final StatisticsService statisticsService;
    protected final EventMapper mapper;

    protected BaseEventService(StatisticsService statisticsService, EventMapper mapper) {
        this.statisticsService = statisticsService;
        this.mapper = mapper;
    }

    protected Map<Long, Long> getEventViews(List<Long> eventsIds, LocalDateTime start, LocalDateTime end) {
        return statisticsService.getEventsViews(eventsIds, start, end, true);
    }

    protected Map<Long, Long> getConfirmedCount(List<Long> eventsIds) {
        return statisticsService.getConfirmedCount(eventsIds);
    }

    protected Long getConfirmedCountOfEvent(Long eventId) {
        return statisticsService.getConfirmedCountForEvent(eventId);
    }

    protected Long getSingleEventViews(Long eventId, LocalDateTime start, LocalDateTime end, boolean unique) {
        return statisticsService.getEventViews(eventId, start, end, unique);
    }

    protected EventFullDto toEventFullDto(Event event) {
        Long eventId = event.getId();
        LocalDateTime start = LocalDateTime.now();
        if (event.getPublishedOn() != null) {
            start = event.getPublishedOn();
        }
        Long eventViews = getSingleEventViews(eventId, start, LocalDateTime.now(), true);
        Long confirmedCount = getConfirmedCountOfEvent(eventId);
        return mapper.mapToFullDto(event, confirmedCount, eventViews);
    }
}
