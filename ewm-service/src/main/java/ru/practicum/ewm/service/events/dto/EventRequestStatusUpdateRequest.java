package ru.practicum.ewm.service.events.dto;

import lombok.Data;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private EventRequestAction status;
}
