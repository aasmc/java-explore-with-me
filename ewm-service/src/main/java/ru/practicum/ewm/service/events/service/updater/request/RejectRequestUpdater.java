package ru.practicum.ewm.service.events.service.updater.request;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventRequestAction;
import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.requests.mapper.RequestsMapper;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;

import java.util.Collections;
import java.util.List;

@Component
public class RejectRequestUpdater extends BaseEventRequestUpdater implements EventRequestUpdater {

    protected RejectRequestUpdater(RequestsRepository requestsRepository, RequestsMapper mapper) {
        super(requestsRepository, mapper);
    }

    @Override
    protected EventRequestStatusUpdateResult updateInternal(List<Request> requests,
                                                         Event event) {
        List<ParticipationRequestDto> rejectedDtos = updateStatusForRequests(requests, ParticipationStatus.REJECTED);
        return createResult(Collections.emptyList(), rejectedDtos);
    }

    @Override
    public EventRequestAction getAction() {
        return EventRequestAction.REJECTED;
    }
}
