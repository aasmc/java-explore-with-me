package ru.practicum.ewm.service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminEventsController {

    @GetMapping("/admin/events")
    public List<EventFullDto> getAllEvents(@RequestParam(value = "users", required = false) List<Long> users,
                                           @RequestParam(value = "states", required = false) List<String> states,
                                           @RequestParam(value = "categories", required = false) List<Long> categories,
                                           @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                           @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                           @RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Received admin request to GET all events.");
        // TODO
        return Collections.emptyList();
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable("eventId") Long eventId,
                                    @RequestBody UpdateEventAdminRequest dto) {
        log.info("Received admin PATCH request to update event with ID={} to new event={}",
                eventId, dto);
        // TODO дата начала изменяемого события должна быть не ранее чем за час от даты публикации. (Ожидается код ошибки 409)
        // TODO событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
        // TODO событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)

        return null;
    }

}
