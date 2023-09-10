package ru.practicum.ewm.service.events.util;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.UpdateEventUserRequest;

import java.time.LocalDateTime;

import static ru.practicum.ewm.service.error.ErrorConstants.EVENT_WRONG_STATE_MSG;
import static ru.practicum.ewm.service.error.ErrorConstants.EVENT_WRONG_UPDATE_DATE_MSG;

@Component
public class UserEventValidator {

    public void validateUpdate(Event event, UpdateEventUserRequest update) {
        validateState(event.getState());
        validateNewDate(update.getEventDate());
    }

    private void validateNewDate(LocalDateTime newDate) {
        if (newDate != null && !isNewEventDateValidForUpdate(newDate)) {
            String msg = String.format(EVENT_WRONG_UPDATE_DATE_MSG, newDate);
            throw EwmServiceException.incorrectParameters(msg);
        }
    }

    private void validateState(EventState state) {
        if (state != null && !isStateValidForUpdate(state)) {
            String msg = String.format(EVENT_WRONG_STATE_MSG, "update", state);
            throw EwmServiceException.wrongConditions(msg);
        }
    }

    private boolean isStateValidForUpdate(EventState state) {
        return state == EventState.PENDING || state == EventState.CANCELED;
    }

    private boolean isNewEventDateValidForUpdate(LocalDateTime newDate) {
        LocalDateTime threshold = LocalDateTime.now().plusHours(2);
        return newDate.isAfter(threshold);
    }

}
