package ru.practicum.ewm.service.events.dto;

import lombok.Data;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private ParticipationStatus status;
}
