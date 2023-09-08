package ru.practicum.ewm.service.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.service.events.mapper.EventMapper;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.events.service.updater.admin.AdminEventUpdater;
import ru.practicum.ewm.service.events.util.AdminEventUpdateValidator;
import ru.practicum.ewm.service.events.util.DateHelper;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.error.ErrorConstants.EVENT_NOT_FOUND_MSG;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminEventsServiceImpl implements AdminEventsService {

    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final StatisticsService statisticsService;
    private final AdminEventUpdateValidator updateValidator;
    private final AdminEventUpdater updater;
    private final EventMapper mapper;
    private final DateHelper dateHelper;


    @Override
    @Transactional(readOnly = true)
    public List<EventFullDto> getAllEvents(List<Long> users,
                                           List<EventState> states,
                                           List<Long> categories,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           int from,
                                           int size) {
        List<Event> events = eventsRepository
                .findAllEventsBy(users, states, categories, rangeStart, rangeEnd, from, size);
        rangeStart = dateHelper.getStartDateOrComputeIfNull(rangeStart, events);
        rangeEnd = dateHelper.getEndDateOrComputeIfNull(rangeEnd);
        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        Map<Long, Long> eventsViews = getEventViews(eventIds, rangeStart, rangeEnd);
        Map<Long, Long> confirmedEventIdToCount = getConfirmedCount(eventIds);
        return mapper.mapToFullDtoList(events, eventsViews, confirmedEventIdToCount);
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest dto) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> {
                    String msg = String.format(EVENT_NOT_FOUND_MSG, eventId);
                    return EwmServiceException.notFoundException(msg);
                });
        updateValidator.validateEventUpdate(event, dto);
        event = updater.updateEvent(event, dto);
        Map<Long, Long> eventViews = getEventViews(List.of(eventId), event.getPublishedOn(), LocalDateTime.now());
        Map<Long, Long> confirmedCount = getConfirmedCount(List.of(eventId));
        return mapper.mapToFullDto(event, confirmedCount.get(eventId), eventViews.get(eventId));
    }

    private Map<Long, Long> getEventViews(List<Long> eventsIds, LocalDateTime start, LocalDateTime end) {
        return statisticsService.getEventsViews(eventsIds, start, end, false);
    }

    private Map<Long, Long> getConfirmedCount(List<Long> eventsIds) {
        return requestsRepository.getEventIdCountByParticipationStatus(ParticipationStatus.CONFIRMED, eventsIds);
    }
}
