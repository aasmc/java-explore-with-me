package ru.practicum.ewm.service.events.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.categories.mapper.CategoriesMapper;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.EventShort;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.LocationDto;
import ru.practicum.ewm.service.usermanagement.dto.UserShortDto;
import ru.practicum.ewm.service.usermanagement.mapper.UserMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CategoriesMapper categoriesMapper;
    private final UserMapper userMapper;

    public List<EventFullDto> mapToFullDtoList(List<Event> events,
                                               Map<Long, Long> eventIdToViews,
                                               Map<Long, Long> confirmedEventIdToCount) {

        return events.stream()
                .map(e -> {
                    Long views = eventIdToViews.getOrDefault(e.getId(), 0L);
                    Long confirmedRequests = confirmedEventIdToCount.getOrDefault(e.getId(), 0L);
                    return mapToFullDto(e, confirmedRequests, views);
                })
                .collect(Collectors.toList());
    }

    public EventFullDto mapToFullDto(Event event, Long confirmedRequests, Long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoriesMapper.mapToDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(userMapper.mapToShortDto(event.getUser()))
                .location(LocationDto.builder()
                        .lat(event.getLocation().getLat())
                        .lon(event.getLocation().getLon())
                        .build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipationLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public List<EventShortDto> mapToShortDtoList(List<EventShort> events,
                                                 Map<Long, Long> eventIdToViews,
                                                 Map<Long, Long> confirmedEventIdToCount) {
        return events.stream()
                .map(e -> {
                    Long views = eventIdToViews.getOrDefault(e.getId(), 0L);
                    Long confirmedRequests = confirmedEventIdToCount.getOrDefault(e.getId(), 0L);
                    return mapToShortDto(e, confirmedRequests, views);
                })
                .collect(Collectors.toList());
    }

    public EventShortDto mapToShortDto(EventShort event, Long confirmedRequests, Long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoriesMapper.mapToDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .initiator(UserShortDto.builder()
                        .id(event.getInitiatorId())
                        .name(event.getInitiatorName()).build())
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
