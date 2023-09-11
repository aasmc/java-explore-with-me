package ru.practicum.ewm.service.events.service.updater.user;

import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.UpdateEventUserRequest;

public interface UserEventUpdater {

    Event updateEvent(Event event, UpdateEventUserRequest update);

}
