package ru.practicum.ewm.service.events.service.publicservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.EventShort;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.EventSort;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.mapper.EventMapper;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.events.service.BaseEventService;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.util.DateHelper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.error.ErrorConstants.EVENT_NOT_FOUND_MSG;

@Service
@Transactional
public class PublicEventsServiceImpl extends BaseEventService implements PublicEventsService {

    private final EventsRepository eventsRepository;
    private final DateHelper dateHelper;

    public PublicEventsServiceImpl(EventsRepository eventsRepository,
                                   StatisticsService statisticsService,
                                   DateHelper dateHelper,
                                   EventMapper mapper) {
        super(statisticsService, mapper);
        this.eventsRepository = eventsRepository;
        this.dateHelper = dateHelper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getAllEvents(String text,
                                            List<Long> categories,
                                            Boolean paid,
                                            LocalDateTime start,
                                            LocalDateTime end,
                                            boolean onlyAvailable,
                                            EventSort sort,
                                            int from,
                                            int size) {
        List<EventShort> events = eventsRepository
                .findAllShortEventsBy(text, categories, paid, start, end, sort, from, size);
        List<Long> eventIds = events.stream().map(EventShort::getId).collect(Collectors.toList());
        start = dateHelper.getEventShortStartDateOrComputeIfNull(start, events);
        end = dateHelper.getEndDateOrComputeIfNull(end);
        Map<Long, Long> eventsViews = getEventViews(eventIds, start, end);
        Map<Long, Long> confirmedEventIdToCount = getConfirmedCount(eventIds);
        events = filterOnlyAvailable(onlyAvailable, events, confirmedEventIdToCount);
        List<EventShortDto> result = mapper.mapToShortDtoList(events, eventsViews, confirmedEventIdToCount);
        if (sort == EventSort.EVENT_VIEWS) {
            result = result.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews).reversed())
                    .collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public EventFullDto getEvent(Long eventId) {
        Event event = eventsRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> {
                    String msg = String.format(EVENT_NOT_FOUND_MSG, eventId);
                    return EwmServiceException.notFoundException(msg);
                });
        return toEventFullDto(event);
    }

    private List<EventShort> filterOnlyAvailable(boolean onlyAvailable, List<EventShort> events, Map<Long, Long> confirmedEventIdToCount) {
        if (onlyAvailable) {
            events = events.stream().filter(e -> {
                int participationLimit = e.getParticipationLimit();
                Long confirmed = confirmedEventIdToCount.getOrDefault(e.getId(), 0L);
                return confirmed < participationLimit;
            }).collect(Collectors.toList());
        }
        return events;
    }
}