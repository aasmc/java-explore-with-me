package ru.practicum.ewm.service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.service.events.service.AdminEventsService;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminEventsController {

    private final DateUtil dateUtil;
    private final AdminEventsService adminEventsService;

    @GetMapping("/admin/events")
    public List<EventFullDto> getAllEvents(@RequestParam(value = "users", required = false) List<Long> users,
                                           @RequestParam(value = "states", required = false) List<EventState> states,
                                           @RequestParam(value = "categories", required = false) List<Long> categories,
                                           @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                           @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                           @RequestParam(value = "from", defaultValue = "0") int from,
                                           @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Received admin request to GET all events.");
        LocalDateTime start = dateUtil.toDate(rangeStart);
        LocalDateTime end = dateUtil.toDate(rangeEnd);
        checkDates(start, end);
        return adminEventsService.getAllEvents(users, states, categories, start, end, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable("eventId") Long eventId,
                                    @RequestBody UpdateEventAdminRequest dto) {
        log.info("Received admin PATCH request to update event with ID={} to new event={}",
                eventId, dto);

        return adminEventsService.updateEvent(eventId, dto);
    }

    private void checkDates(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(LocalDateTime.now()) || start.isAfter(end)) {
            String msg = String.format("Invalid date parameters: %s, %s",
                    start.format(DateUtil.FORMATTER),
                    end.format(DateUtil.FORMATTER));
            throw EwmServiceException.incorrectParameters(msg);
        }
    }

}
