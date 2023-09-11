package ru.practicum.ewm.service.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.service.requests.domain.EventStatusCount;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequester_Id(Long userId);

    List<Request> findAllByRequester_Id_AndEvent_Id(Long userId, Long eventId);

    List<Request> findAllByEvent_User_IdAndEvent_Id(Long userId, Long eventId);

    List<Request> findAllByIdInAndEvent_User_IdAndEvent_Id(List<Long> requestIds, Long userId, Long eventId);

    @Query("select new ru.practicum.ewm.service.requests.domain.EventStatusCount(r.event.id, count(r.id)) " +
            "from Request r " +
            "where r.status = :status " +
            "and r.event.id in :eventIds " +
            "group by r.event.id")
    List<EventStatusCount> countByStatus(@Param("status") ParticipationStatus status, @Param("eventIds") List<Long> eventIds);

    default Map<Long, Long> getEventIdCountByParticipationStatus(ParticipationStatus status, List<Long> eventIds) {
        return countByStatus(status, eventIds).stream()
                .collect(Collectors.toMap(EventStatusCount::getEventId,
                        EventStatusCount::getCount,
                        Long::sum,
                        HashMap::new));
    }
}
