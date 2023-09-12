package ru.practicum.ewm.service.events.service.publicservice;

import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.EventSort;

import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventsService {

    List<EventShortDto> getAllEvents(String text,
                                     List<Long> categories,
                                     Boolean paid,
                                     LocalDateTime start,
                                     LocalDateTime end,
                                     boolean onlyAvailable,
                                     EventSort sort,
                                     int from,
                                     int size);

    EventFullDto getEvent(Long eventId);

    List<EventShortDto> getAllEventsInLocationWithId(long locationId,
                                                     String text,
                                                     List<Long> categories,
                                                     Boolean paid,
                                                     LocalDateTime start,
                                                     LocalDateTime end,
                                                     boolean onlyAvailable,
                                                     EventSort sort,
                                                     int from,
                                                     int size);

    List<EventShortDto> getAllEventsInLocationWithCoords(float lat,
                                                         float lon,
                                                         String text,
                                                         List<Long> categories,
                                                         Boolean paid,
                                                         LocalDateTime start,
                                                         LocalDateTime end,
                                                         boolean onlyAvailable,
                                                         EventSort sort,
                                                         int from,
                                                         int size);

}
