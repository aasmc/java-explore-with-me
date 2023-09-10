package ru.practicum.ewm.service.events.service.updater.request;

import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventRequestAction;
import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.requests.domain.Request;

import java.util.List;

public interface EventRequestUpdater {

    EventRequestStatusUpdateResult updateRequests(List<Request> requests,
                                                  Event event);

    EventRequestAction getAction();

}
