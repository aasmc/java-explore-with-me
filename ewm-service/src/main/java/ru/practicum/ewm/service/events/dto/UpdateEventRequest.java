package ru.practicum.ewm.service.events.dto;

import java.time.LocalDateTime;

public interface UpdateEventRequest {

    String getAnnotation();

    Long getCategory();

    String getDescription();

    LocationDto getLocation();

    LocalDateTime getEventDate();

    Boolean getPaid();

    Integer getParticipantLimit();

    Boolean getRequestModeration();

    String getTitle();

}
