package ru.practicum.ewm.service.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.compilations.domain.Compilation;
import ru.practicum.ewm.service.compilations.dto.CompilationDto;
import ru.practicum.ewm.service.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.ewm.service.compilations.mapper.CompilationsMapper;
import ru.practicum.ewm.service.compilations.repository.CompilationsRepository;
import ru.practicum.ewm.service.compilations.service.updater.CompilationUpdater;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.repository.EventsRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ru.practicum.ewm.service.error.ErrorConstants.COMPILATION_CREATION_FAILED_EVENTS_NOT_FOUND;
import static ru.practicum.ewm.service.error.ErrorConstants.COMPILATION_NOT_FOUND_MSG;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationsAdminServiceImpl implements CompilationsAdminService {

    private final CompilationUpdater updater;
    private final EventsRepository eventsRepository;
    private final CompilationsRepository compilationsRepository;
    private final CommonCompilationService commonService;
    private final CompilationsMapper mapper;

    @Override
    public CompilationDto createCompilation(NewCompilationDto dto) {
        Set<Event> events = getCompilationEvents(dto.getEvents());
        Compilation compilation = mapper.mapToDomain(dto, events);
        Compilation saved = compilationsRepository.save(compilation);
        setCompilationToEvents(compilation, events);
        return commonService.toCompilationDto(saved);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationsRepository.findById(compId)
                .ifPresentOrElse(c -> compilationsRepository.deleteById(compId),
                        () -> {
                            String msg = String.format(COMPILATION_NOT_FOUND_MSG, compId);
                            throw EwmServiceException.notFoundException(msg);
                        });
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest dto) {
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> {
                    String msg = String.format(COMPILATION_NOT_FOUND_MSG, compId);
                    return EwmServiceException.notFoundException(msg);
                });
        Set<Event> events = dto.getEvents() == null ? null : getCompilationEvents(dto.getEvents());
        Compilation updated = updater.updateCompilation(compilation, dto, events);
        updated = compilationsRepository.save(updated);
        return commonService.toCompilationDto(updated);
    }

    private Set<Event> getCompilationEvents(Set<Long> eventIds) {
        Set<Event> events = Collections.emptySet();
        if (null != eventIds && !eventIds.isEmpty()) {
            checkAllEventsExist(eventIds);
            events = eventsRepository.findAllByIdIn(List.copyOf(eventIds));
        }
        return events;
    }

    private void setCompilationToEvents(Compilation compilation, Set<Event> events) {
        if (events != null && !events.isEmpty()) {
            events.forEach(event -> event.setCompilation(compilation));
        }
    }

    private void checkAllEventsExist(Set<Long> eventIds) {
        Long eventsCount = eventsRepository.countAllByIdIn(List.copyOf(eventIds));
        if (eventsCount != eventIds.size()) {
            throw EwmServiceException.wrongConditions(COMPILATION_CREATION_FAILED_EVENTS_NOT_FOUND);
        }
    }
}
