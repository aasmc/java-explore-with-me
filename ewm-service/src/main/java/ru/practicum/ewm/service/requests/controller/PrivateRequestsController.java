package ru.practicum.ewm.service.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.requests.service.RequestsService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PrivateRequestsController {

    private final RequestsService requestsService;

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable("userId") Long userId) {
        log.info("Received private request to GET all participation requests of user with ID={}", userId);
        return requestsService.getRequestsOfUser(userId);
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable("userId") Long userId,
                                                 @RequestParam("eventId") Long eventId) {
        log.info("Received private request to POST a participation request for event with ID={} by user with ID={}",
                eventId, userId);
        return requestsService.createRequest(userId, eventId);
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        log.info("Received private PATCH request to cancel participation request with ID={}, by user with ID={}",
                requestId, userId);
        return requestsService.cancelRequest(userId, requestId);
    }

}
