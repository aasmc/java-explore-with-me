package ru.practicum.ewm.service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.events.dto.*;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PrivateEventsController {

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getEventsForUser(@PathVariable("userId") Long userId,
                                                @Valid @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                                @Valid @Min(1) @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Received private request to GET events of user with ID={}. From: {}, size: {}",
                userId, from, size);
        // TODO
        return Collections.emptyList();
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable("userId") Long userId,
                                    @Valid @RequestBody NewEventDto dto) {
        log.info("Received private POST request to create event for user with ID={}," +
                "new event: {}", userId, dto);
        // TODO don't forget about eventDate in future and custom exception
        // TODO дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
        return null;
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventForUser(@PathVariable("userId") Long userId,
                                        @PathVariable("eventId") Long eventId) {
        log.info("Received private request to GET event with ID={} for user with ID={}.",
                eventId, userId);
        // TODO
        return null;
    }

    @PatchMapping("/users/{userId}/events{eventId}")
    public EventFullDto updateEvent(@PathVariable("userId") Long userId,
                                    @PathVariable("eventId") Long eventId,
                                    @RequestBody UpdateEventUserRequest dto) {
        log.info("Received private PATCH request to update event with ID={} of user with ID={}. To be updated to: {}",
                eventId, userId, dto);
        // TODO can update ONLY PENDING or CANCELED events! (expected status in case of error 409)
        // TODO eventDate cannot be before LocalDateTime.now().plusHours(2). (expected status in case of error 409
        return null;
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequests(@PathVariable("userId") Long userId,
                                                                  @PathVariable("eventId") Long eventId) {
        log.info("Received private request to GET participation requests for event with ID={}, created by user with ID={}",
                eventId, userId);
        // TODO
        return Collections.emptyList();
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateParticipationRequests(@PathVariable("userId") Long userId,
                                                                      @PathVariable("eventId") Long eventId,
                                                                      @RequestBody EventRequestStatusUpdateRequest dto) {
        log.info("Received private PATCH request to update participation requests. EventId={}, UserId={}, Dto: {}",
                eventId, userId, dto);
        // TODO если для события лимит заявок равен 0 или отключена пре-модерация заявок, то подтверждение заявок не требуется
        // TODO нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие (Ожидается код ошибки 409)
        // TODO статус можно изменить только у заявок, находящихся в состоянии ожидания (Ожидается код ошибки 409)
        // TODO если при подтверждении данной заявки, лимит заявок для события исчерпан, то все неподтверждённые заявки необходимо отклонить

        return null;
    }
}
