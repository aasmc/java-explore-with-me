package ru.practicum.ewm.service.events.util;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.AdminEventStateAction;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;

import static ru.practicum.ewm.service.error.ErrorConstants.*;
import static ru.practicum.ewm.service.events.dto.AdminEventStateAction.*;
import static ru.practicum.ewm.service.events.dto.EventState.*;

@Component
public class AdminEventUpdateValidator {

    /**
     * Checks if it is allowed to update the event. If not, throws
     * EwmServiceException with HttpStatus.CONFLICT.
     */
    public void validateEventUpdate(Event event, UpdateEventAdminRequest request) {
        checkEventPublishDate(event, request.getEventDate());
        checkPublish(request.getStateAction(), event);
        checkReject(request.getStateAction(), event);
    }

    /**
     * New event start date cannot be less than 1 hour before event publish date.
     * If not the method throws EwmServiceException with HttpStatus.CONFLICT.
     */
    private void checkEventPublishDate(Event event, LocalDateTime newDate) {
        if (newEventDateIncorrect(event.getPublishedOn(), newDate)) {
            String msg = String.format(EVENT_WRONG_UPDATE_DATE_MSG, newDate.toString());
            throw EwmServiceException.wrongConditions(msg);
        }
    }

    private boolean newEventDateIncorrect(LocalDateTime publishDate, LocalDateTime newEventDate) {
        return publishDate != null && newEventDate.plusHours(1).isAfter(publishDate);
    }

    /**
     * Event can be published only if it is in PENDING state.
     * If not the method throws EwmServiceException with HttpStatus.CONFLICT.
     */
    private void checkPublish(AdminEventStateAction action, Event event) {
        if (publishNotAllowed(action, event.getState())) {
            String msg = String.format(EVENT_WRONG_STATE_MSG, "publish", event.getState());
            throw EwmServiceException.wrongConditions(msg);
        }
    }

    private boolean publishNotAllowed(AdminEventStateAction action, EventState eventState) {
        return action == PUBLISH_EVENT && eventState != PENDING;
    }

    /**
     * Event can be rejected only if it has not been published yet.
     */
    private void checkReject(AdminEventStateAction action, Event event) {
        if (cancelNotAllowed(action, event.getState())) {
            String msg = String.format(EVENT_WRONG_STATE_MSG, "cancel", event.getState());
            throw EwmServiceException.wrongConditions(msg);
        }
    }

    private boolean cancelNotAllowed(AdminEventStateAction action, EventState eventState) {
        return action == REJECT_EVENT && eventState == PUBLISHED;
    }

}
