package ru.practicum.ewm.service.events.service.updater.user;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.UpdateEventUserRequest;
import ru.practicum.ewm.service.events.dto.UserEventStateAction;
import ru.practicum.ewm.service.events.service.updater.BaseUpdater;
import ru.practicum.ewm.service.events.util.UserEventValidator;

@Component
public class UserEventUpdaterImpl extends BaseUpdater implements UserEventUpdater {

    private final UserEventValidator validator;

    public UserEventUpdaterImpl(CategoriesRepository categoriesRepository, UserEventValidator validator) {
        super(categoriesRepository);
        this.validator = validator;
    }

    @Override
    public Event updateEvent(Event event, UpdateEventUserRequest update) {
        validator.validateUpdate(event, update);
        updateAnnotation(event, update.getAnnotation());
        updateCategory(event, update.getCategory());
        updateDescription(event, update.getDescription());
        updateEventDate(event, update.getEventDate());
        updateLocation(event, update.getLocation());
        updatePaid(event, update.getPaid());
        updateParticipationLimit(event, update.getParticipantLimit());
        updateRequestModeration(event, update.getRequestModeration());
        updateState(event, update.getStateAction());
        updateTitle(event, update.getTitle());
        return event;
    }

    private void updateState(Event event, UserEventStateAction action) {
        if (action != null) {
            switch (action) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
                default:
                    throw new RuntimeException("Unknown UserEventStateAction " + action);
            }
        }
    }

}
