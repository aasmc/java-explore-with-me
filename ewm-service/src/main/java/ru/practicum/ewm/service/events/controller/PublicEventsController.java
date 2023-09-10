package ru.practicum.ewm.service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.EventSort;
import ru.practicum.ewm.service.events.service.publicservice.PublicEventsService;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicEventsController {

    private final StatisticsService statisticsService;
    private final PublicEventsService publicEventsService;
    private final DateUtil dateUtil;
    @Value("${spring.application.name:ewm-service}")
    private String appName;

    @GetMapping("/events")
    public List<EventShortDto> getAllEvents(@RequestParam(value = "text", required = false) String text,
                                            @RequestParam(value = "categories", required = false) List<Long> categories,
                                            @RequestParam(value = "paid", required = false) Boolean paid,
                                            @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                            @RequestParam(value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                            @RequestParam(value = "sort", required = false) EventSort sort,
                                            @RequestParam(value = "from", defaultValue = "0") int from,
                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                            HttpServletRequest request) {
        log.info("Received public request to GET all events");
        saveStatistics(request);
        LocalDateTime start = dateUtil.toDate(rangeStart);
        LocalDateTime end = dateUtil.toDate(rangeEnd);
        checkDates(start, end);
        return publicEventsService
                .getAllEvents(text, categories, paid, start, end, onlyAvailable, sort, from, size);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable("id") Long id, HttpServletRequest request) {
        log.info("Received public request to GET event by id={}", id);
        saveStatistics(request);
        return publicEventsService.getEvent(id);
    }

    private void saveStatistics(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        String endpoint = request.getRequestURI();
        statisticsService.saveStatistics(appName, endpoint, ip, LocalDateTime.now());
    }

    private void checkDates(LocalDateTime start, LocalDateTime end) {
        if (start != null && end != null && (start.isAfter(LocalDateTime.now()) || start.isAfter(end))) {
            String msg = String.format("Invalid date parameters: %s, %s",
                    start.format(DateUtil.FORMATTER),
                    end.format(DateUtil.FORMATTER));
            throw EwmServiceException.incorrectParameters(msg);
        }
    }
}
