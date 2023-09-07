package ru.practicum.ewm.service.events.service;

import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventsService {

    List<EventFullDto> getAllEvents(List<Long> users,
                                    List<EventState> states,
                                    List<Long> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    int from,
                                    int size);


    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest dto);

}
