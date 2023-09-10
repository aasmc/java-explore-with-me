package ru.practicum.ewm.service.events.service.updater.request;

import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.requests.mapper.RequestsMapper;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;

import java.util.List;

import static ru.practicum.ewm.service.error.ErrorConstants.EVENT_REQUEST_UPDATE_NOT_ALLOWED_MSG;

public abstract class BaseEventRequestUpdater implements EventRequestUpdater {

    protected final RequestsRepository requestsRepository;
    protected final RequestsMapper mapper;

    protected BaseEventRequestUpdater(RequestsRepository requestsRepository,
                                      RequestsMapper requestsMapper) {
        this.requestsRepository = requestsRepository;
        this.mapper = requestsMapper;
    }

    @Override
    public EventRequestStatusUpdateResult updateRequests(List<Request> requests, Event event) {
        checkCanUpdate(requests);
        return updateInternal(requests, event);
    }

    protected abstract EventRequestStatusUpdateResult updateInternal(List<Request> requests, Event event);

    protected List<ParticipationRequestDto> updateStatusForRequests(List<Request> requests,
                                                                     ParticipationStatus status) {
        requests.forEach(r -> r.setStatus(status));
        List<Request> updated = requestsRepository.saveAll(requests);
        return mapper.mapToDtoList(updated);
    }

    protected EventRequestStatusUpdateResult createResult(List<ParticipationRequestDto> confirmed,
                                                          List<ParticipationRequestDto> rejected) {
        return EventRequestStatusUpdateResult
                .builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
    }

    private void checkCanUpdate(List<Request> requests) {
        if (notAllowedToUpdate(requests)) {
            throw EwmServiceException.wrongConditions(EVENT_REQUEST_UPDATE_NOT_ALLOWED_MSG);
        }
    }

    private boolean notAllowedToUpdate(List<Request> requests) {
        return requests.stream()
                .anyMatch(r -> r.getStatus() != ParticipationStatus.PENDING);
    }
}
