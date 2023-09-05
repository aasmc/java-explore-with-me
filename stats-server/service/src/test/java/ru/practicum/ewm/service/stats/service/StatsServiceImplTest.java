package ru.practicum.ewm.service.stats.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.service.stats.common.dto.StatRequest;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;
import ru.practicum.ewm.service.stats.mapper.StatsMapper;
import ru.practicum.ewm.service.stats.model.Stats;
import ru.practicum.ewm.service.stats.storage.StatsRepository;

import java.util.List;

import static ru.practicum.ewm.service.stats.testutil.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceImplTest {

    @Mock
    private StatsRepository statsRepository;
    @Mock
    private StatsMapper statsMapper;
    @InjectMocks
    private StatsServiceImpl statsService;

    private StatRequest request = StatRequest.builder()
            .app(APP)
            .ip(ONE_IP)
            .uri(ONE_URL)
            .timestamp(ONE_TIMESTAMP)
            .build();

    private Stats oneStats = Stats.builder()
            .app(APP)
            .ip(ONE_IP)
            .uri(ONE_URL)
            .timestamp(ONE_TIMESTAMP)
            .build();

    private List<StatResponse> responses = List.of(
            new StatResponse(APP, ONE_URL, 3L),
            new StatResponse(APP, FOUR_URL, 2L),
            new StatResponse(APP, SIX_URL, 1L)
    );

    @BeforeEach
    void setup() {
        Mockito
                .lenient()
                .when(statsMapper.mapToDomain(request)).thenReturn(oneStats);

        Mockito
                .lenient()
                .when(statsRepository.findAllStatsUnique(ONE_TIMESTAMP, SIX_TIMESTAMP, null))
                .thenReturn(responses);

        Mockito
                .lenient()
                .when(statsRepository.findAllStats(ONE_TIMESTAMP, SIX_TIMESTAMP, null))
                .thenReturn(responses);

        Mockito
                .lenient()
                .when(statsRepository.findAllStatsUnique(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, FOUR_URL, SIX_URL)))
                .thenReturn(responses);

        Mockito
                .lenient()
                .when(statsRepository.findAllStats(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, FOUR_URL, SIX_URL)))
                .thenReturn(responses);
    }

    @Test
    void getStatistics_hasUris_notUnique_callsCorrectMethod() {
        statsService.getStatistics(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, FOUR_URL, SIX_URL), false);
        Mockito
                .verify(statsRepository, Mockito.times(1))
                .findAllStats(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, FOUR_URL, SIX_URL));
    }

    @Test
    void getStatistics_hasUris_unique_callsCorrectMethod() {
        statsService.getStatistics(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, FOUR_URL, SIX_URL), true);
        Mockito
                .verify(statsRepository, Mockito.times(1))
                .findAllStatsUnique(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, FOUR_URL, SIX_URL));
    }

    @Test
    void getStatistics_nullUris_notUnique_callsCorrectMethod() {
        statsService.getStatistics(ONE_TIMESTAMP, SIX_TIMESTAMP, null, false);
        Mockito
                .verify(statsRepository, Mockito.times(1))
                .findAllStats(ONE_TIMESTAMP, SIX_TIMESTAMP, null);
    }

    @Test
    void getStatistics_nullUris_unique_callsCorrectMethod() {
        statsService.getStatistics(ONE_TIMESTAMP, SIX_TIMESTAMP, null, true);
        Mockito
                .verify(statsRepository, Mockito.times(1))
                .findAllStatsUnique(ONE_TIMESTAMP, SIX_TIMESTAMP, null);
    }

    @Test
    void saveStat_callsRepoMethod() {
        statsService.saveStat(request);

        Mockito
                .verify(statsMapper, Mockito.times(1)).mapToDomain(request);

        Mockito
                .verify(statsRepository, Mockito.times(1)).save(oneStats);
    }
}