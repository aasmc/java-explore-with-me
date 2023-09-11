package ru.practicum.ewm.service.events.service.privateservice;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventRequestAction;
import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.events.service.updater.request.EventRequestUpdater;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.requests.service.RequestsService;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.error.ErrorConstants.EVENT_NOT_FOUND_MSG;
import static ru.practicum.ewm.service.error.ErrorConstants.USER_NOT_FOUND_MSG;

@Service
@Transactional
public class PrivateEventRequestsServiceImpl implements PrivateEventRequestsService {

    private final RequestsService requestsService;
    private final UsersRepository usersRepository;
    private final EventsRepository eventsRepository;
    private final Map<EventRequestAction, EventRequestUpdater> updatersMap;

    public PrivateEventRequestsServiceImpl(RequestsService requestsService,
                                           UsersRepository usersRepository,
                                           EventsRepository eventsRepository,
                                           List<EventRequestUpdater> updaters) {
        this.requestsService = requestsService;
        this.usersRepository = usersRepository;
        this.eventsRepository = eventsRepository;
        this.updatersMap = updaters.stream()
                .collect(Collectors.toMap(EventRequestUpdater::getAction, Function.identity()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId) {
        return requestsService.getRequestDtosForEventOfUser(userId, eventId);
    }

    @Override
    public EventRequestStatusUpdateResult updateParticipationRequests(Long userId,
                                                                      Long eventId,
                                                                      EventRequestStatusUpdateRequest dto) {
        checkUserExists(userId);

        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> {
                    String msg = String.format(EVENT_NOT_FOUND_MSG, eventId);
                    return EwmServiceException.notFoundException(msg);
                });
        List<Request> requests = requestsService
                .getRequestsForEventOfUser(userId, eventId, dto.getRequestIds());
        EventRequestUpdater updater = getUpdater(dto);
        return updater.updateRequests(requests, event);
    }

    private EventRequestUpdater getUpdater(EventRequestStatusUpdateRequest dto) {
        return Optional.ofNullable(updatersMap.get(dto.getStatus()))
                .orElseThrow(() -> new RuntimeException("Unknown ParticipationStatus: " + dto.getStatus()));
    }

    private void checkUserExists(Long userId) {
        if (!usersRepository.existsById(userId)) {
            String msg = String.format(USER_NOT_FOUND_MSG, userId);
            throw EwmServiceException.notFoundException(msg);
        }
    }
}
