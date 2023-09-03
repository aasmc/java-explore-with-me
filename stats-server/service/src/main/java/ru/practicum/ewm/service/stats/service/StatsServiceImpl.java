package ru.practicum.ewm.service.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.stats.common.dto.StatRequest;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;
import ru.practicum.ewm.service.stats.mapper.StatsMapper;
import ru.practicum.ewm.service.stats.storage.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsMapper statsMapper;

    @Override
    public void saveStat(StatRequest request) {
        statsRepository.save(statsMapper.mapToDomain(request));
    }

    @Override
    public List<StatResponse> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            return statsRepository.findAllStatsUnique(start, end, uris);
        } else {
            return statsRepository.findAllStats(start, end, uris);
        }
    }
}
