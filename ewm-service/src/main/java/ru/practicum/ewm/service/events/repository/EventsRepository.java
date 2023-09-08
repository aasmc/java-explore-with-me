package ru.practicum.ewm.service.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventState;

import java.util.Optional;

public interface EventsRepository extends JpaRepository<Event, Long>, CustomEventsRepository {

    Optional<Event> findByIdAndState(Long id, EventState state);

}
