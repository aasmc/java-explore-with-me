package ru.practicum.ewm.service.events.service.updater.admin;

import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;

public interface AdminEventUpdater {

    Event updateEvent(Event event, UpdateEventAdminRequest update);

}
