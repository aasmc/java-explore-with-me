package ru.practicum.ewm.service.events.repository;

import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.EventShort;
import ru.practicum.ewm.service.events.dto.EventSort;
import ru.practicum.ewm.service.events.dto.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomEventsRepository {

    List<Event> findAllEventsBy(List<Long> users,
                                List<EventState> states,
                                List<Long> categories,
                                LocalDateTime start,
                                LocalDateTime end,
                                int from,
                                int size);

    List<EventShort> findAllShortEventsBy(String text,
                                          List<Long> categories,
                                          Boolean paid,
                                          LocalDateTime start,
                                          LocalDateTime end,
                                          EventSort sort,
                                          int from,
                                          int size);

}
