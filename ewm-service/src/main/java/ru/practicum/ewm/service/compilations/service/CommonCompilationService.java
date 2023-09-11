package ru.practicum.ewm.service.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.compilations.domain.Compilation;
import ru.practicum.ewm.service.compilations.dto.CompilationDto;
import ru.practicum.ewm.service.compilations.mapper.CompilationsMapper;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.util.DateHelper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonCompilationService {

    private final StatisticsService statisticsService;
    private final CompilationsMapper mapper;
    private final DateHelper dateHelper;

    protected List<CompilationDto> toCompilationDtoList(List<Compilation> savedCompilations) {
        List<Event> events = savedCompilations.stream()
                .flatMap(c -> c.getEvents().stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        Map<Long, Long> eventIdToConfirmed = getEventConfirmations(eventIds);
        Map<Long, Long> eventIdToViews = getEventViews(events, eventIds);
        return mapper.mapToDtoList(savedCompilations, eventIdToViews, eventIdToConfirmed);
    }

    protected CompilationDto toCompilationDto(Compilation saved) {
        if (saved.getEvents().isEmpty()) {
            return mapper.mapToDto(saved, Collections.emptyMap(), Collections.emptyMap());
        }
        List<Long> eventIds = saved.getEvents().stream()
                .map(Event::getId).collect(Collectors.toList());
        List<Event> events = new ArrayList<>(saved.getEvents());
        Map<Long, Long> eventIdToConfirmed = getEventConfirmations(eventIds);
        Map<Long, Long> eventIdToViews = getEventViews(events, eventIds);
        return mapper.mapToDto(saved, eventIdToViews, eventIdToConfirmed);
    }

    private Map<Long, Long> getEventViews(List<Event> events, List<Long> eventIds) {
        LocalDateTime start = dateHelper.getStartDateOrComputeIfNull(null, events);
        LocalDateTime end = dateHelper.getEndDateOrComputeIfNull(null);
        return statisticsService
                .getEventsViews(eventIds, start, end, true);
    }

    private Map<Long, Long> getEventConfirmations(List<Long> eventIds) {
        return statisticsService.getConfirmedCount(eventIds);
    }
}
