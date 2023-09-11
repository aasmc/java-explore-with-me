package ru.practicum.ewm.service.events.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests;
    private List<ParticipationRequestDto> rejectedRequests;
}
