package ru.practicum.ewm.service.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.EventSort;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PublicEventsController {

    @GetMapping("/events")
    public List<EventShortDto> getAllEvents(@RequestParam(value = "text", required = false) String text,
                                            @RequestParam(value = "categories", required = false) List<Long> categories,
                                            @RequestParam(value = "paid", required = false) boolean paid,
                                            @RequestParam(value = "rangeStart", required = false) String rangeStart,
                                            @RequestParam(value = "rangeEnd", required = false) String rangeEnd,
                                            @RequestParam(value = "onlyAvailable", defaultValue = "false") boolean onlyAvailable,
                                            @RequestParam(value = "sort", defaultValue = "EVENT_DATE") EventSort sort,
                                            @RequestParam(value = "from", defaultValue = "0") int from,
                                            @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Received public request to GET all events");
        // TODO в выдаче должны быть только опубликованные события
        // TODO текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
        // TODO если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать события, которые произойдут позже текущей даты и времени
        // TODO информация о каждом событии должна включать в себя количество просмотров и количество уже одобренных заявок на участие
        // TODO информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        return Collections.emptyList();
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable("id") Long id) {
        log.info("Received public request to GET event by id={}", id);
        // TODO событие должно быть опубликовано
        // TODO информация о событии должна включать в себя количество просмотров и количество подтвержденных запросов
        // TODO информацию о том, что по этому эндпоинту был осуществлен и обработан запрос, нужно сохранить в сервисе статистики
        return null;
    }
}
