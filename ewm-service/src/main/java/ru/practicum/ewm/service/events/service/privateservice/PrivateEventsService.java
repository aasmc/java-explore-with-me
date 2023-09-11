package ru.practicum.ewm.service.events.service.privateservice;

import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.NewEventDto;
import ru.practicum.ewm.service.events.dto.UpdateEventUserRequest;

import java.util.List;

public interface PrivateEventsService {

    List<EventShortDto> getEventsOfUser(Long userId, int from, int size);

    EventFullDto createEvent(Long userId, NewEventDto dto);

    EventFullDto getEventOfUser(Long userId, Long eventId);

    EventFullDto updateEventOfUser(Long userId, Long eventId, UpdateEventUserRequest dto);

}
