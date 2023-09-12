package ru.practicum.ewm.service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.EventSort;
import ru.practicum.ewm.service.events.service.publicservice.PublicEventsService;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.util.DateHelper;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicEventsController {

    private final StatisticsService statisticsService;
    private final PublicEventsService publicEventsService;
    private final DateHelper dateHelper;
    @Value("${spring.application.name:ewm-service}")
    private String appName;

    /**
     * Возвращает список событий, которые проводятся в локации с идентификатором [locationId].
     * Если локации с таким ID не существует - возвращает ошибку 404.
     * Пользователь может создать событие без привязки к существующим локациям.
     */
    @GetMapping("/events/location/{locId}")
    public List<EventShortDto> getAllEventsInLocationById(@PathVariable("locId") long locationId,
                                                          @RequestParam(value = "text", required = false) String text,
                                                          @RequestParam(value = "categories", required = false) List<Long> categories,
                                                          @RequestParam(value = "paid", required = false) Boolean paid,
                                                          @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                                          @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                                          @RequestParam(value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                                          @RequestParam(value = "sort", required = false) EventSort sort,
                                                          @RequestParam(value = "from", defaultValue = "0") int from,
                                                          @RequestParam(value = "size", defaultValue = "10") int size,
                                                          HttpServletRequest request) {
        log.info("Received public request to GET all events in location with id={}", locationId);
        saveStatistics(request);
        LocalDateTime start = dateHelper.toDate(rangeStart);
        LocalDateTime end = dateHelper.toDate(rangeEnd);
        dateHelper.checkDates(start, end);
        List<EventShortDto> events = publicEventsService
                .getAllEventsInLocationWithId(locationId, text, categories, paid, start, end, onlyAvailable, sort, from, size);
        log.info("Retrieved events for location with ID={}. Events: {}", locationId, events);
        return events;
    }

    /**
     * Возвращает список событий, которые проводятся в локации с координатами [lat] [lon].
     * Если локации с такими координатами не существует - возвращает пустой список.
     * Пользователь может создать событие без привязки к существующим локациям.
     */
    @GetMapping("/events/location")
    public List<EventShortDto> getAllEventsInLocationByCoords(@RequestParam("lat") float lat,
                                                              @RequestParam("lon") float lon,
                                                              @RequestParam(value = "text", required = false) String text,
                                                              @RequestParam(value = "categories", required = false) List<Long> categories,
                                                              @RequestParam(value = "paid", required = false) Boolean paid,
                                                              @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                                              @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                                              @RequestParam(value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                                              @RequestParam(value = "sort", required = false) EventSort sort,
                                                              @RequestParam(value = "from", defaultValue = "0") int from,
                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                              HttpServletRequest request) {
        log.info("Received public request to GET all events in location with coordinates: lat={}, lon={}", lat, lon);
        saveStatistics(request);
        LocalDateTime start = dateHelper.toDate(rangeStart);
        LocalDateTime end = dateHelper.toDate(rangeEnd);
        dateHelper.checkDates(start, end);
        List<EventShortDto> events = publicEventsService
                .getAllEventsInLocationWithCoords(lat, lon, text, categories, paid, start, end, onlyAvailable, sort, from, size);
        log.info("Retrieved events for location with lat:{}, lon:{}. Events: {}", lat, lon, events);
        return events;
    }

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
        LocalDateTime start = dateHelper.toDate(rangeStart);
        LocalDateTime end = dateHelper.toDate(rangeEnd);
        dateHelper.checkDates(start, end);
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
}
