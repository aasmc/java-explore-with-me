package ru.practicum.ewm.service.events.util;

import org.junit.jupiter.api.Test;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.AdminEventStateAction;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.practicum.ewm.service.error.ErrorConstants.CONDITIONS_NOT_MET_REASON;
import static ru.practicum.ewm.service.util.TestConstants.EVENT_PUBLISHED_ON;
import static ru.practicum.ewm.service.util.TestData.*;

class AdminEventUpdateValidatorTest {

    private final AdminEventUpdateValidator validator = new AdminEventUpdateValidator();

    @Test
    void validateEventUpdate_allOk_notThrows() {
        boolean passed = true;
        Event event = defaultPendingEvent();
        event.setState(EventState.PENDING);
        event.setPublishedOn(EVENT_PUBLISHED_ON);
        UpdateEventAdminRequest dto = updateEventAdminRequest();
        dto.setEventDate(EVENT_PUBLISHED_ON.plusHours(2));
        dto.setStateAction(AdminEventStateAction.REJECT_EVENT);
        assertTrue(passed);
    }

    @Test
    void validateEventUpdate_statePUBLISHED_actionREJECT_EVENT_throws() {
        Event event = defaultPendingEvent();
        event.setState(EventState.PUBLISHED);
        UpdateEventAdminRequest dto = updateEventAdminRequest();
        dto.setEventDate(null);
        dto.setStateAction(AdminEventStateAction.REJECT_EVENT);

        EwmServiceException ex = assertThrows(EwmServiceException.class,
                () -> validator.validateEventUpdate(event, dto));
        assertThat(ex.getReason()).isEqualTo(CONDITIONS_NOT_MET_REASON);
    }

    @Test
    void validateEventUpdate_stateNotPENDING_actionPUBLISH_EVENT_throws() {
        Event event = defaultPendingEvent();
        event.setState(EventState.PUBLISHED);
        UpdateEventAdminRequest dto = updateEventAdminRequest();
        dto.setEventDate(null);
        dto.setStateAction(AdminEventStateAction.PUBLISH_EVENT);

        EwmServiceException ex = assertThrows(EwmServiceException.class,
                () -> validator.validateEventUpdate(event, dto));
        assertThat(ex.getReason()).isEqualTo(CONDITIONS_NOT_MET_REASON);
    }

    @Test
    void validateEventUpdate_publishDateWrong_throws() {
        Event event = defaultPendingEvent();
        event.setPublishedOn(EVENT_PUBLISHED_ON);
        UpdateEventAdminRequest dto = updateEventAdminRequest();
        dto.setEventDate(event.getPublishedOn().plusMinutes(10));
        dto.setStateAction(null);

        EwmServiceException ex = assertThrows(EwmServiceException.class,
                () -> validator.validateEventUpdate(event, dto));
        assertThat(ex.getReason()).isEqualTo(CONDITIONS_NOT_MET_REASON);
    }

}