package ru.practicum.ewm.service.stats.service;

import ru.practicum.ewm.service.stats.common.dto.StatRequest;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void saveStat(StatRequest request);

    List<StatResponse> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

}
