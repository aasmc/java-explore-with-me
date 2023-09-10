package ru.practicum.ewm.service.events.service.updater.admin;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.AdminEventStateAction;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.service.events.service.updater.BaseUpdater;
import ru.practicum.ewm.service.events.util.AdminEventUpdateValidator;

import java.time.LocalDateTime;

@Component
public class AdminEventUpdaterImpl extends BaseUpdater implements AdminEventUpdater {

    private final AdminEventUpdateValidator validator;

    public AdminEventUpdaterImpl(CategoriesRepository categoriesRepository, AdminEventUpdateValidator validator) {
        super(categoriesRepository);
        this.validator = validator;
    }

    @Override
    public Event updateEvent(Event event, UpdateEventAdminRequest update) {
        validator.validateEventUpdate(event, update);
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

    private void updateState(Event event, AdminEventStateAction action) {
        if (action != null) {
            switch (action) {
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                default:
                    throw new RuntimeException("Unknown AdminEventStateAction: " + action);
            }
        }
    }
}
