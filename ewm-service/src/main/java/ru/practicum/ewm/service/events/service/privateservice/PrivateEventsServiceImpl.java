package ru.practicum.ewm.service.events.service.privateservice;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.EventShort;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.NewEventDto;
import ru.practicum.ewm.service.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.service.events.mapper.EventMapper;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.events.service.BaseEventService;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.events.service.updater.user.UserEventUpdater;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;
import ru.practicum.ewm.service.util.DateHelper;
import ru.practicum.ewm.service.util.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.error.ErrorConstants.*;

@Service
@Transactional
public class PrivateEventsServiceImpl extends BaseEventService implements PrivateEventsService {

    private final EventsRepository eventsRepository;
    private final DateHelper dateHelper;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;
    private final UserEventUpdater updater;

    public PrivateEventsServiceImpl(EventsRepository eventsRepository,
                                    EventMapper mapper,
                                    StatisticsService statisticsService,
                                    DateHelper dateHelper,
                                    CategoriesRepository categoriesRepository,
                                    UsersRepository usersRepository,
                                    UserEventUpdater updater) {
        super(statisticsService, mapper);
        this.eventsRepository = eventsRepository;
        this.dateHelper = dateHelper;
        this.categoriesRepository = categoriesRepository;
        this.usersRepository = usersRepository;
        this.updater = updater;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EventShortDto> getEventsOfUser(Long userId, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        List<EventShort> events = eventsRepository.findShortEventsOfUser(userId, pageable);
        LocalDateTime start = dateHelper.getEventShortStartDateOrComputeIfNull(null, events);
        LocalDateTime end = dateHelper.getEndDateOrComputeIfNull(null);
        List<Long> eventIds = events.stream().map(EventShort::getId).collect(Collectors.toList());
        Map<Long, Long> eventViews = getEventViews(eventIds, start, end);
        Map<Long, Long> confirmedCount = getConfirmedCount(eventIds);
        return mapper.mapToShortDtoList(events, eventViews, confirmedCount);
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto dto) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> {
                    String msg = String.format(USER_NOT_FOUND_MSG, userId);
                    return EwmServiceException.notFoundException(msg);
                });
        Category category = categoriesRepository.findById(dto.getCategory())
                .orElseThrow(() -> {
                    String msg = String.format(CATEGORY_NOT_FOUND_MSG, dto.getCategory());
                    return EwmServiceException.notFoundException(msg);
                });
        Event event = mapper.mapToDomain(dto, category, user);
        event = eventsRepository.save(event);
        return toEventFullDto(event);
    }

    @Override
    @Transactional(readOnly = true)
    public EventFullDto getEventOfUser(Long userId, Long eventId) {
        Event event = eventsRepository.findEventByIdAndUser_Id(eventId, userId)
                .orElseThrow(() -> {
                    String msg = String.format(EVENT_NOT_FOUND_MSG, eventId);
                    return EwmServiceException.notFoundException(msg);
                });
        return toEventFullDto(event);
    }

    @Override
    public EventFullDto updateEventOfUser(Long userId, Long eventId, UpdateEventUserRequest dto) {
        checkUserExists(userId);
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> {
                    String msg = String.format(EVENT_NOT_FOUND_MSG, eventId);
                    return EwmServiceException.notFoundException(msg);
                });
        Event updated = updater.updateEvent(event, dto);
        eventsRepository.saveAndFlush(updated);
        return toEventFullDto(updated);
    }

    private void checkUserExists(Long userId) {
        if (!usersRepository.existsById(userId)) {
            String msg = String.format(USER_NOT_FOUND_MSG, userId);
            throw EwmServiceException.notFoundException(msg);
        }
    }
}
