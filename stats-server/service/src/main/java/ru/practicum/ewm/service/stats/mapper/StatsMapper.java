package ru.practicum.ewm.service.stats.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.stats.common.dto.StatRequest;
import ru.practicum.ewm.service.stats.model.Stats;

@Component
public class StatsMapper {
    public Stats mapToDomain(StatRequest request) {
        return Stats.builder()
                .app(request.getApp())
                .uri(request.getUri())
                .ip(request.getIp())
                .timestamp(request.getTimestamp())
                .build();
    }
}
