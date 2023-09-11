package ru.practicum.ewm.service.compilations.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.compilations.domain.Compilation;
import ru.practicum.ewm.service.compilations.dto.CompilationDto;
import ru.practicum.ewm.service.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.mapper.EventMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationsMapper {

    private final EventMapper eventMapper;

    public List<CompilationDto> mapToDtoList(List<Compilation> compilations,
                                             Map<Long, Long> eventIdToViews,
                                             Map<Long, Long> eventIdToConfirmed) {
        return compilations.stream().map(c -> mapToDto(c, eventIdToViews, eventIdToConfirmed))
                .collect(Collectors.toList());
    }

    public CompilationDto mapToDto(Compilation compilation,
                                   Map<Long, Long> eventIdToViews,
                                   Map<Long, Long> eventIdToConfirmed) {
        List<EventShortDto> events = Collections.emptyList();
        if (!compilation.getEvents().isEmpty()) {
            events = compilation.getEvents().stream().map(e -> {
                Long views = eventIdToViews.getOrDefault(e.getId(), 0L);
                Long confirmed = eventIdToConfirmed.getOrDefault(e.getId(), 0L);
                return eventMapper.mapToShortDto(e, confirmed, views);
            }).collect(Collectors.toList());
        }
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(events)
                .pinned(compilation.getPinned())
                .build();
    }

    public Compilation mapToDomain(NewCompilationDto dto, Set<Event> events) {
        return Compilation.builder()
                .pinned(dto.isPinned())
                .title(dto.getTitle())
                .events(events)
                .build();
    }
}
