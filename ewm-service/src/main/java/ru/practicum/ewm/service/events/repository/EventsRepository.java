package ru.practicum.ewm.service.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "where ((:users) is null or e.user.id in :users) " +
            "and ((:states) is null or e.state in :states) " +
            "and ((:categories) is null or e.category.id in :categories) " +
            "and ((:start is null and :end is null) " +
            "or e.eventDate between :start and :end)")
    List<Event> findAllEventsBy(@Param("users") List<Long> users,
                                @Param("states") List<EventState> states,
                                @Param("categories") List<Long> categories,
                                @Param("start")LocalDateTime start,
                                @Param("end") LocalDateTime end,
                                Pageable pageable);

}
