package ru.practicum.ewm.service.compilations.service.updater;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.compilations.domain.Compilation;
import ru.practicum.ewm.service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.ewm.service.events.domain.Event;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CompilationUpdater {

    public Compilation updateCompilation(Compilation compilation,
                                         UpdateCompilationRequest dto,
                                         Set<Event> events) {
        updatePinned(compilation, dto.getPinned());
        updateTitle(compilation, dto.getTitle());
        updateEvents(compilation, events);
        return compilation;
    }

    private void updatePinned(Compilation compilation, Boolean pinned) {
        if (null != pinned) {
            compilation.setPinned(pinned);
        }
    }

    private void updateTitle(Compilation compilation, String title) {
        if (null != title) {
            compilation.setTitle(title);
        }
    }

    private void updateEvents(Compilation compilation, Set<Event> events) {
        if (events != null) {
            compilation.setEvents(events);
            events.forEach(event -> event.setCompilation(compilation));
        }
    }

}
