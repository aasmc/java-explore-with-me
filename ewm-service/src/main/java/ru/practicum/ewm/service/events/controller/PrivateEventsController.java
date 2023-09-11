package ru.practicum.ewm.service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.events.dto.*;
import ru.practicum.ewm.service.events.service.privateservice.PrivateEventRequestsService;
import ru.practicum.ewm.service.events.service.privateservice.PrivateEventsService;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.util.DateHelper;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PrivateEventsController {

    private final PrivateEventsService privateEventService;
    private final PrivateEventRequestsService eventRequestsService;
    private final DateHelper dateHelper;

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsOfUser(@PathVariable("userId") Long userId,
                                                @Valid @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                                @Valid @Min(1) @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Received private request to GET events of user with ID={}. From: {}, size: {}",
                userId, from, size);
        return privateEventService.getEventsOfUser(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") Long userId,
                                    @Valid @RequestBody NewEventDto dto) {
        log.info("Received private POST request to create event for user with ID={}," +
                "new event: {}", userId, dto);
        dateHelper.checkNewEventDate(dto.getEventDate());
        return privateEventService.createEvent(userId, dto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventOfUser(@PathVariable("userId") Long userId,
                                        @PathVariable("eventId") Long eventId) {
        log.info("Received private request to GET event with ID={} for user with ID={}.",
                eventId, userId);
        return privateEventService.getEventOfUser(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @RequestBody @Valid UpdateEventUserRequest dto) {
        log.info("Received private PATCH request to update event with ID={} of user with ID={}. To be updated to: {}",
                eventId, userId, dto);
        return privateEventService.updateEventOfUser(userId, eventId, dto);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable("userId") Long userId,
                                                                  @PathVariable("eventId") Long eventId) {
        log.info("Received private request to GET participation requests for event with ID={}, created by user with ID={}",
                eventId, userId);
        return eventRequestsService.getParticipationRequests(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipationRequests(@PathVariable("userId") Long userId,
                                                                      @PathVariable("eventId") Long eventId,
                                                                      @RequestBody EventRequestStatusUpdateRequest dto) {
        log.info("Received private PATCH request to update participation requests. EventId={}, UserId={}, Dto: {}",
                eventId, userId, dto);
        return eventRequestsService.updateParticipationRequests(userId, eventId, dto);
    }
}
