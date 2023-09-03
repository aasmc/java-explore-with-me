package ru.practicum.ewm.service.stats.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;
import ru.practicum.ewm.service.stats.model.Stats;
import ru.practicum.ewm.service.stats.testutil.StatsContainer;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.ewm.service.stats.testutil.TestConstants.*;
import static ru.practicum.ewm.service.stats.testutil.TestData.*;

@ActiveProfiles("integtest")
@DataJpaTest
@Testcontainers
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class StatsRepositoryTest {

    @Container
    static PostgreSQLContainer<StatsContainer> container = StatsContainer.getInstance();

    private final StatsRepository statsRepository;

    @BeforeEach
    void initDb() {
        statsRepository.save(ONE);
        statsRepository.save(TWO);
        statsRepository.save(THREE);
        statsRepository.save(FOUR);
        statsRepository.save(FIVE);
        statsRepository.save(SIX);
    }

    @AfterEach
    void clearDb() {
        statsRepository.deleteAll();
    }

    @Test
    void findAllStatsForUris_someUrisIncluded_returnsCorrectList() {
        List<StatResponse> stats = statsRepository.findAllStatsForUris(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, SIX_URL));
        assertThat(stats).hasSize(2);
        assertThat(stats.get(0).getHits()).isEqualTo(3);
        assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);
        assertThat(stats.get(1).getHits()).isEqualTo(1);
        assertThat(stats.get(1).getUri()).isEqualTo(SIX_URL);
    }

    @Test
    void findAllStatsForUris_allUrisIncluded_returnsCorrectList() {
        List<StatResponse> stats = statsRepository.findAllStatsForUris(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, FOUR_URL, SIX_URL));
        assertThat(stats).hasSize(3);
        assertThat(stats.get(0).getHits()).isEqualTo(3);
        assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);
        assertThat(stats.get(1).getHits()).isEqualTo(2);
        assertThat(stats.get(1).getUri()).isEqualTo(FOUR_URL);
        assertThat(stats.get(2).getHits()).isEqualTo(1);
        assertThat(stats.get(2).getUri()).isEqualTo(SIX_URL);
    }

    @Test
    void findAllStatsForUrisUnique_someUrisIncluded_returnsCorrectList() {
        List<StatResponse> stats = statsRepository.findAllStatsForUrisUnique(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, TWO_URL_SAME_AS_ONE, THREE_URL_SAME_AS_ONE));
        assertThat(stats).hasSize(1);
        assertThat(stats.get(0).getHits()).isEqualTo(2);
        assertThat(stats.get(0).getApp()).isEqualTo(APP);
        assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);
    }

    @Test
    void findAllStatsForUrisUnique_allUrisIncluded_returnsCorrectList() {
        List<StatResponse> stats = statsRepository.findAllStatsForUrisUnique(ONE_TIMESTAMP, SIX_TIMESTAMP, List.of(ONE_URL, FOUR_URL, SIX_URL));
        assertThat(stats).hasSize(3);
        assertThat(stats.get(0).getHits()).isEqualTo(2);
        assertThat(stats.get(0).getApp()).isEqualTo(APP);
        assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);

        assertThat(stats.get(1).getHits()).isEqualTo(1);
        assertThat(stats.get(2).getHits()).isEqualTo(1);
    }

    @Test
    void findAllStats_whenDatesInRangeOfSaved_ReturnsCorrectList() {
        List<StatResponse> stats = statsRepository.findAllStats(ONE_TIMESTAMP, SIX_TIMESTAMP);
        assertThat(stats).hasSize(3);
        assertThat(stats.get(0).getHits()).isEqualTo(3);
        assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);
        assertThat(stats.get(1).getHits()).isEqualTo(2);
        assertThat(stats.get(1).getUri()).isEqualTo(FOUR_URL);
        assertThat(stats.get(2).getHits()).isEqualTo(1);
        assertThat(stats.get(2).getUri()).isEqualTo(SIX_URL);
    }

    @Test
    void findAllStatsUnique_returnsCorrectList() {
        List<StatResponse> stats = statsRepository.findAllStatsUnique(ONE_TIMESTAMP, THREE_TIMESTAMP);
        assertThat(stats).hasSize(1);
        assertThat(stats.get(0).getHits()).isEqualTo(2);
        assertThat(stats.get(0).getApp()).isEqualTo(APP);
        assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);
    }

    @Test
    void findAllStatsUnique_whenDatesNotInRangeOfSaved_returnsCorrectList() {
        List<StatResponse> stats = statsRepository.findAllStatsUnique(ONE_TIMESTAMP.minusDays(100), SIX_TIMESTAMP.minusDays(50));
        assertThat(stats).isEmpty();
    }

    @Test
    void findAllStatsUnique_whenDatesInRangeOfSaved_returnsCorrectList() {
        List<StatResponse> stats = statsRepository.findAllStatsUnique(ONE_TIMESTAMP, SIX_TIMESTAMP);
        assertThat(stats).hasSize(3);
        assertThat(stats.get(0).getHits()).isEqualTo(2);
        assertThat(stats.get(0).getApp()).isEqualTo(APP);
        assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);

        assertThat(stats.get(1).getHits()).isEqualTo(1);
        assertThat(stats.get(2).getHits()).isEqualTo(1);
    }

    @Test
    void save_savesStatisticsToDb() {
        Stats stats = Stats.builder()
                .app("app")
                .uri("/uri")
                .ip("0.0.0.0")
                .timestamp(LocalDateTime.of(2023, Month.SEPTEMBER, 2, 12, 0, 0))
                .build();

        Stats saved = statsRepository.save(stats);
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getApp()).isEqualTo(stats.getApp());
        assertThat(saved.getUri()).isEqualTo(stats.getUri());
        assertThat(saved.getIp()).isEqualTo(stats.getIp());
        assertThat(saved.getTimestamp()).isEqualTo(stats.getTimestamp());
    }
}