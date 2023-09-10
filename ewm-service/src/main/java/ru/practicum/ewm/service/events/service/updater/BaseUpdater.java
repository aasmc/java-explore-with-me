package ru.practicum.ewm.service.events.service.updater;

import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.Location;
import ru.practicum.ewm.service.events.dto.LocationDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.service.error.ErrorConstants.CATEGORY_NOT_FOUND_MSG;

public abstract class BaseUpdater {
    private final CategoriesRepository categoriesRepository;

    protected BaseUpdater(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    protected void updateAnnotation(Event event, String newAnnotation) {
        if (newAnnotation != null) {
            event.setAnnotation(newAnnotation);
        }
    }

    protected void updateCategory(Event event, Long categoryId) {
        if (categoryId != null) {
            Category category = categoriesRepository.findById(categoryId)
                    .orElseThrow(() -> {
                        String msg = String.format(CATEGORY_NOT_FOUND_MSG, categoryId);
                        return EwmServiceException.notFoundException(msg);
                    });
            event.setCategory(category);
        }
    }

    protected void updateDescription(Event event, String newDescription) {
        if (newDescription != null) {
            event.setDescription(newDescription);
        }
    }

    protected void updateEventDate(Event event, LocalDateTime newDate) {
        if (newDate != null) {
            event.setEventDate(newDate);
        }
    }

    protected void updateLocation(Event event, LocationDto location) {
        if (location != null) {
            event.setLocation(Location.builder()
                    .lat(location.getLat())
                    .lon(location.getLon())
                    .build());
        }
    }

    protected void updatePaid(Event event, Boolean paid) {
        if (paid != null) {
            event.setPaid(paid);
        }
    }

    protected void updateParticipationLimit(Event event, Integer participationLimit) {
        if (participationLimit != null) {
            event.setParticipationLimit(participationLimit);
        }
    }

    protected void updateRequestModeration(Event event, Boolean requestModeration) {
        if (requestModeration != null) {
            event.setRequestModeration(requestModeration);
        }
    }

    protected void updateTitle(Event event, String newTitle) {
        if (newTitle != null) {
            event.setTitle(newTitle);
        }
    }
}
