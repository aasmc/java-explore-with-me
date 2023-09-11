package ru.practicum.ewm.service.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;

import static ru.practicum.ewm.service.error.ErrorConstants.*;

@Component
@RequiredArgsConstructor
public class RequestValidator {

    private final StatisticsService statisticsService;
    private final UsersRepository usersRepository;
    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;

    public Event getEventOrThrow(Long eventId) {
        return eventsRepository.findById(eventId)
                .orElseThrow(() -> {
                    String msg = String.format(EVENT_NOT_FOUND_MSG, eventId);
                    return EwmServiceException.wrongConditions(msg);
                });
    }

    public User getUserOrThrow(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> {
                    String msg = String.format(USER_NOT_FOUND_MSG, userId);
                    return EwmServiceException.notFoundException(msg);
                });
    }

    public Request getRequestOrThrow(Long requestId) {
        return requestsRepository.findById(requestId)
                .orElseThrow(() -> {
                    String msg = String.format(REQUEST_NOT_FOUND_MSG, requestId);
                    return EwmServiceException.notFoundException(msg);
                });
    }

    public void validateParticipationLimitNotReached(Event event) {
        if (event.getParticipationLimit() != 0 || !event.getRequestModeration()) {
            Long confirmed = statisticsService.getConfirmedCountForEvent(event.getId());
            if (confirmed >= event.getParticipationLimit()) {
                throw EwmServiceException.wrongConditions(EVENT_PARTICIPATION_LIMIT_REACHED);
            }
        }
    }

    public void validateUserExists(Long userId) {
        if (!usersRepository.existsById(userId)) {
            String msg = String.format(USER_NOT_FOUND_MSG, userId);
            throw EwmServiceException.notFoundException(msg);
        }
    }

    public void validateEventPublished(Event event) {
        if (event.getState() != EventState.PUBLISHED) {
            String msg = String.format(EVENT_NOT_PUBLISHED_MSG, event.getId());
            throw EwmServiceException.wrongConditions(msg);
        }
    }

    public void validateUserNotInitiator(Long userId, Event event) {
        if (event.getUser().getId().equals(userId)) {
            String msg = String.format(EVENT_INITIATOR_SELF_REQUEST_MSG, userId, event.getId());
            throw EwmServiceException.wrongConditions(msg);
        }
    }
}
