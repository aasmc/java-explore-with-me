package ru.practicum.ewm.service.requests.service;

import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestsService {

    List<ParticipationRequestDto> getRequestDtosForEventOfUser(Long userId, Long eventId);

    List<Request> getRequestsForEventOfUser(Long userId, Long eventId, List<Long> requestIds);

    List<ParticipationRequestDto> getRequestsOfUser(Long userId);

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

}
