package ru.practicum.ewm.service.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PrivateRequestsController {

    @GetMapping("/users/{userId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable("userId") Long userId) {
        log.info("Received private request to GET all participation requests of user with ID={}", userId);
        // TODO
        return Collections.emptyList();
    }

    @PostMapping("/users/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable("userId") Long userId,
                                                 @RequestParam("eventId") Long eventId) {
        log.info("Received private request to POST a participation request for event with ID={} by user with ID={}",
                eventId, userId);
        // TODO нельзя добавить повторный запрос (Ожидается код ошибки 409)
        // TODO инициатор события не может добавить запрос на участие в своём событии (Ожидается код ошибки 409)
        // TODO нельзя участвовать в неопубликованном событии (Ожидается код ошибки 409)
        // TODO если у события достигнут лимит запросов на участие - необходимо вернуть ошибку (Ожидается код ошибки 409)
        // TODO если для события отключена пре-модерация запросов на участие, то запрос должен автоматически перейти в состояние подтвержденного
        return null;
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable("userId") Long userId,
                                                 @PathVariable("requestId") Long requestId) {
        log.info("Received private PATCH request to cancel participation request with ID={}, by user with ID={}",
                requestId, userId);
        // TODO

        return null;
    }

}
