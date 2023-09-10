package ru.practicum.ewm.service.events.service.updater.request;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventRequestAction;
import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.requests.mapper.RequestsMapper;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;

import java.util.Collections;
import java.util.List;

import static ru.practicum.ewm.service.error.ErrorConstants.EVENT_PARTICIPATION_LIMIT_REACHED;
import static ru.practicum.ewm.service.requests.dto.ParticipationStatus.CONFIRMED;
import static ru.practicum.ewm.service.requests.dto.ParticipationStatus.REJECTED;

@Component
public class ConfirmRequestUpdater extends BaseEventRequestUpdater implements EventRequestUpdater {

    private final StatisticsService statisticsService;


    public ConfirmRequestUpdater(RequestsRepository requestsRepository,
                                 RequestsMapper mapper,
                                 StatisticsService statisticsService) {
        super(requestsRepository, mapper);
        this.statisticsService = statisticsService;
    }

    @Override
    public EventRequestAction getAction() {
        return EventRequestAction.CONFIRMED;
    }

    @Override
    protected EventRequestStatusUpdateResult updateInternal(List<Request> requests, Event event) {
        if (eventHasNoRestrictionsOnParticipants(event)) {
            List<ParticipationRequestDto> confirmed = updateStatusForRequests(requests, CONFIRMED);
            return createResult(confirmed, Collections.emptyList());
        }
        return performConfirmation(requests, event);
    }

    private EventRequestStatusUpdateResult performConfirmation(List<Request> requests, Event event) {
        if (requests.isEmpty()) {
            return createResult(Collections.emptyList(), Collections.emptyList());
        }
        Long confirmedParticipants = statisticsService.getConfirmedCountForEvent(event.getId());
        checkCanBeginConfirmation(event, confirmedParticipants);
        int participantLimit = event.getParticipationLimit();
        int requestIdx = 0;
        for (long i = confirmedParticipants + 1; i <= participantLimit && requestIdx < requests.size(); ++i) {
            Request request = requests.get(requestIdx++);
            request.setStatus(CONFIRMED);
        }
        int confirmedBorderIdx = requestIdx;
        for (; requestIdx < requests.size(); ++requestIdx) {
            Request request = requests.get(requestIdx++);
            request.setStatus(REJECTED);
        }

        List<Request> updated = requestsRepository.saveAll(requests);

        List<Request> confirmed = updated.subList(0, confirmedBorderIdx);
        List<Request> rejected = updated.subList(confirmedBorderIdx, requests.size());

        List<ParticipationRequestDto> confirmedDtos = mapper.mapToDtoList(confirmed);
        List<ParticipationRequestDto> rejectedDtos = mapper.mapToDtoList(rejected);
        return createResult(confirmedDtos, rejectedDtos);
    }

    private boolean eventHasNoRestrictionsOnParticipants(Event event) {
        return event.getParticipationLimit() == 0 || !event.getRequestModeration();
    }

    private void checkCanBeginConfirmation(Event event, Long confirmedParticipants) {
        if (hasReachedParticipantLimit(event, confirmedParticipants)) {
            throw EwmServiceException.wrongConditions(EVENT_PARTICIPATION_LIMIT_REACHED);
        }
    }

    private boolean hasReachedParticipantLimit(Event event, Long confirmedParticipants) {
        return confirmedParticipants >= event.getParticipationLimit();
    }
}
