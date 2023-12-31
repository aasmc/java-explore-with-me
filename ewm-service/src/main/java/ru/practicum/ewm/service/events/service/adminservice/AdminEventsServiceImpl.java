package ru.practicum.ewm.service.events.service.adminservice;

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
import ru.practicum.ewm.service.events.service.CommonEventsService;
import ru.practicum.ewm.service.events.service.updater.admin.AdminEventUpdater;
import ru.practicum.ewm.service.util.DateHelper;

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
    private final AdminEventUpdater updater;
    private final DateHelper dateHelper;
    private final CommonEventsService commonEventsService;
    private final EventMapper mapper;


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
        Map<Long, Long> eventsViews = commonEventsService.getEventViews(eventIds, rangeStart, rangeEnd);
        Map<Long, Long> confirmedEventIdToCount = commonEventsService.getConfirmedCount(eventIds);
        return mapper.mapToFullDtoList(events, eventsViews, confirmedEventIdToCount);
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest dto) {
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> {
                    String msg = String.format(EVENT_NOT_FOUND_MSG, eventId);
                    return EwmServiceException.notFoundException(msg);
                });
        event = updater.updateEvent(event, dto);
        return commonEventsService.toEventFullDto(event);
    }

}
