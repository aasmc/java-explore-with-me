package ru.practicum.ewm.service.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.events.domain.Event;

public interface EventsRepository extends JpaRepository<Event, Long> {

}
