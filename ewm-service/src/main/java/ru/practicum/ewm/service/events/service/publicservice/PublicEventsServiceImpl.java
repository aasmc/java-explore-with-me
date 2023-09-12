package ru.practicum.ewm.service.events.service.publicservice;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.ewm.service.events.service.CommonEventsService;
import ru.practicum.ewm.service.locations.domain.Location;
import ru.practicum.ewm.service.locations.repository.LocationsRepository;
import ru.practicum.ewm.service.util.DateHelper;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.error.ErrorConstants.EVENT_NOT_FOUND_MSG;
import static ru.practicum.ewm.service.error.ErrorConstants.LOCATION_NOT_FOUND_MSG;

@Service
@Transactional
@RequiredArgsConstructor
public class PublicEventsServiceImpl implements PublicEventsService {

    private final EventsRepository eventsRepository;
    private final DateHelper dateHelper;
    private final EventMapper mapper;
    private final CommonEventsService commonEventsService;
    private final LocationsRepository locationsRepository;

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
        return createResult(events, start, end, onlyAvailable, sort);
    }


    @Override
    public EventFullDto getEvent(Long eventId) {
        Event event = eventsRepository.findByIdAndState(eventId, EventState.PUBLISHED)
                .orElseThrow(() -> {
                    String msg = String.format(EVENT_NOT_FOUND_MSG, eventId);
                    return EwmServiceException.notFoundException(msg);
                });
        return commonEventsService.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllEventsInLocationWithId(long locationId,
                                                            String text,
                                                            List<Long> categories,
                                                            Boolean paid,
                                                            LocalDateTime start,
                                                            LocalDateTime end,
                                                            boolean onlyAvailable,
                                                            EventSort sort,
                                                            int from,
                                                            int size) {
        Location location = locationsRepository.findById(locationId)
                .orElseThrow(() -> {
                    String msg = String.format(LOCATION_NOT_FOUND_MSG, locationId);
                    return EwmServiceException.notFoundException(msg);
                });
        List<EventShort> events = eventsRepository
                .findAllEventsByLocation(location, text, categories, paid, start, end, sort, from, size);
        return createResult(events, start, end, onlyAvailable, sort);
    }

    @Override
    public List<EventShortDto> getAllEventsInLocationWithCoords(float lat,
                                                                float lon,
                                                                String text,
                                                                List<Long> categories,
                                                                Boolean paid,
                                                                LocalDateTime start,
                                                                LocalDateTime end,
                                                                boolean onlyAvailable,
                                                                EventSort sort,
                                                                int from,
                                                                int size) {
        Optional<Location> locationOpt = locationsRepository.findByLatAndLon(lat, lon);
        if (locationOpt.isEmpty()) {
            return Collections.emptyList();
        }
        List<EventShort> events = eventsRepository
                .findAllEventsByLocation(locationOpt.get(), text, categories, paid, start, end, sort, from, size);
        return createResult(events, start, end, onlyAvailable, sort);
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

    private List<EventShortDto> createResult(List<EventShort> events,
                                             LocalDateTime start,
                                             LocalDateTime end,
                                             boolean onlyAvailable,
                                             EventSort sort) {
        List<Long> eventIds = events.stream().map(EventShort::getId).collect(Collectors.toList());
        start = dateHelper.getEventShortStartDateOrComputeIfNull(start, events);
        end = dateHelper.getEndDateOrComputeIfNull(end);
        Map<Long, Long> eventsViews = commonEventsService.getEventViews(eventIds, start, end);
        Map<Long, Long> confirmedEventIdToCount = commonEventsService.getConfirmedCount(eventIds);
        events = filterOnlyAvailable(onlyAvailable, events, confirmedEventIdToCount);
        List<EventShortDto> result = mapper.mapToShortDtoList(events, eventsViews, confirmedEventIdToCount);
        if (sort == EventSort.EVENT_VIEWS) {
            result = result.stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews).reversed())
                    .collect(Collectors.toList());
        }
        return result;
    }
}
