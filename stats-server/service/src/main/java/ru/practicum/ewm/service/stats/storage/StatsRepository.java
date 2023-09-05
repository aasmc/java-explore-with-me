package ru.practicum.ewm.service.stats.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;
import ru.practicum.ewm.service.stats.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query("select new ru.practicum.ewm.service.stats.common.dto.StatResponse(s.app, s.uri, count(distinct s.ip)) " +
            "from Stats s " +
            "where s.timestamp between :start and :end " +
            "and ((:uris) is null or s.uri in :uris) " +
            "group by s.app, s.uri " +
            "order by count(distinct s.ip) desc")
    List<StatResponse> findAllStatsUnique(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end,
                                          @Param("uris") List<String> uris);

    @Query("select new ru.practicum.ewm.service.stats.common.dto.StatResponse(s.app, s.uri, count(s.ip)) " +
            "from Stats s " +
            "where s.timestamp between :start and :end " +
            "and ((:uris) is null or s.uri in :uris) " +
            "group by s.app, s.uri " +
            "order by count(s.ip) desc")
    List<StatResponse> findAllStats(@Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end,
                                    @Param("uris") List<String> uris);

}
