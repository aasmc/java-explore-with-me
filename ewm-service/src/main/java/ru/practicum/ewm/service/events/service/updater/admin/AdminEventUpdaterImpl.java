package ru.practicum.ewm.service.events.service.updater.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.Location;
import ru.practicum.ewm.service.events.dto.AdminEventStateAction;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.LocationDto;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;

import static ru.practicum.ewm.service.error.ErrorConstants.CATEGORY_NOT_FOUND_MSG;

@Component
@RequiredArgsConstructor
public class AdminEventUpdaterImpl implements AdminEventUpdater {

    private final CategoriesRepository categoriesRepository;

    @Override
    public Event updateEvent(Event event, UpdateEventAdminRequest update) {
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

    private void updateAnnotation(Event event, String newAnnotation) {
        if (newAnnotation != null) {
            event.setAnnotation(newAnnotation);
        }
    }

    private void updateCategory(Event event, Long categoryId) {
        if (categoryId != null) {
            Category category = categoriesRepository.findById(categoryId)
                    .orElseThrow(() -> {
                        String msg = String.format(CATEGORY_NOT_FOUND_MSG, categoryId);
                        return EwmServiceException.notFoundException(msg);
                    });
            event.setCategory(category);
        }
    }

    private void updateDescription(Event event, String newDescription) {
        if (newDescription != null) {
            event.setDescription(newDescription);
        }
    }

    private void updateEventDate(Event event, LocalDateTime newDate) {
        if (newDate != null) {
            event.setEventDate(newDate);
        }
    }

    private void updateLocation(Event event, LocationDto location) {
        if (location != null) {
            event.setLocation(Location.builder()
                    .lat(location.getLat())
                    .lon(location.getLon())
                    .build());
        }
    }

    private void updatePaid(Event event, Boolean paid) {
        if (paid != null) {
            event.setPaid(paid);
        }
    }

    private void updateParticipationLimit(Event event, Integer participationLimit) {
        if (participationLimit != null) {
            event.setParticipationLimit(participationLimit);
        }
    }

    private void updateRequestModeration(Event event, Boolean requestModeration) {
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
    }

    private void updateState(Event event, AdminEventStateAction action) {
        if (action != null) {
            switch (action) {
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    break;
                default:
                    throw new RuntimeException("Unknown AdminEventStateAction: " + action);
            }
        }
    }

    private void updateTitle(Event event, String newTitle) {
        if (newTitle != null) {
            event.setTitle(newTitle);
        }
    }
}
