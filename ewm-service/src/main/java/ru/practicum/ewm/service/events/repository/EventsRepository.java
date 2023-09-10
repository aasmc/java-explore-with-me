package ru.practicum.ewm.service.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.EventShort;
import ru.practicum.ewm.service.events.dto.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EventsRepository extends JpaRepository<Event, Long>, CustomEventsRepository {

    Optional<Event> findByIdAndState(Long id, EventState state);

    Optional<Event> findEventByIdAndUser_Id(Long eventId, Long userId);

    @Query("select new ru.practicum.ewm.service.events.domain.EventShort( " +
            "e.id, " +
            "e.annotation, " +
            "e.category, " +
            "e.eventDate, " +
            "e.user.id, " +
            "e.user.name, " +
            "e.paid, " +
            "e.title, " +
            "e.participationLimit, " +
            "e.publishedOn) " +
            "from Event e " +
            "where e.user.id = :userId")
    List<EventShort> findShortEventsOfUser(@Param("userId") Long userId, Pageable pageable);

    Long countAllByIdIn(List<Long> ids);

    Set<Event> findAllByIdIn(List<Long> ids);
}
