package ru.practicum.ewm.service.stats.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.ewm.service.stats.common.dto.StatRequest;
import ru.practicum.ewm.service.stats.model.Stats;

import java.time.LocalDateTime;
import java.time.Month;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class StatsMapperTest {

    private final StatsMapper mapper = new StatsMapper();

    @Test
    void testMapperCorrect() {
        String app = "app";
        String uri = "/uri";
        String ip = "/ip";
        LocalDateTime timestamp = LocalDateTime.of(2023, Month.SEPTEMBER, 1, 23, 30, 30);


        Stats expected = Stats.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timestamp)
                .build();
        StatRequest request = StatRequest.builder()
                .app(app)
                .ip(ip)
                .uri(uri)
                .timestamp(timestamp)
                .build();
        Stats actual = mapper.mapToDomain(request);
        assertThat(actual.getApp()).isEqualTo(expected.getApp());
        assertThat(actual.getIp()).isEqualTo(expected.getIp());
        assertThat(actual.getUri()).isEqualTo(expected.getUri());
        assertThat(actual.getTimestamp()).isEqualTo(expected.getTimestamp());
    }

}