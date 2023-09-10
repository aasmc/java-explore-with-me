package ru.practicum.ewm.service.compilations.service;

import ru.practicum.ewm.service.compilations.domain.Compilation;
import ru.practicum.ewm.service.compilations.dto.CompilationDto;
import ru.practicum.ewm.service.compilations.mapper.CompilationsMapper;
import ru.practicum.ewm.service.compilations.repository.CompilationsRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.util.DateHelper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseCompilationService {

    protected final StatisticsService statisticsService;
    protected final CompilationsMapper mapper;
    protected final CompilationsRepository compilationsRepository;
    protected final DateHelper dateHelper;

    protected BaseCompilationService(StatisticsService statisticsService,
                                     CompilationsMapper mapper,
                                     CompilationsRepository compilationsRepository,
                                     DateHelper dateHelper) {
        this.statisticsService = statisticsService;
        this.mapper = mapper;
        this.compilationsRepository = compilationsRepository;
        this.dateHelper = dateHelper;
    }

    protected List<CompilationDto> toCompilationDtoList(List<Compilation> savedCompilations) {
        List<Event> events = savedCompilations.stream().flatMap(c -> {
                    Set<Event> cEvents = c.getEvents();
                    if (cEvents == null) {
                        return null;
                    } else {
                        return cEvents.stream();
                    }
                })
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
        if (saved.getEvents() == null || saved.getEvents().isEmpty()) {
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
