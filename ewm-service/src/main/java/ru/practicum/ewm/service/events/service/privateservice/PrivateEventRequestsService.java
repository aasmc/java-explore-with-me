package ru.practicum.ewm.service.events.service.privateservice;

import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateEventRequestsService {

    List<ParticipationRequestDto> getParticipationRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateParticipationRequests(Long userId,
                                                               Long eventId,
                                                               EventRequestStatusUpdateRequest dto);

}
