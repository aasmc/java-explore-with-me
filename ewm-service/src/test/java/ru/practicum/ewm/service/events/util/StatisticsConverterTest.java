package ru.practicum.ewm.service.events.util;

import org.junit.jupiter.api.Test;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class StatisticsConverterTest {

    private final StatisticsConverter converter = new StatisticsConverter();

    @Test
    void convertEventsStatistics_correct() {
        Map<String, Long> uriToEventId = Map.of("/events/1", 1L,
                "/events/2", 2L,
                "/events/3", 3L,
                "/events/4", 4L
        );

        List<StatResponse> stats = List.of(
                StatResponse.builder().uri("/events/1").hits(10L).build(),
                StatResponse.builder().uri("/events/2").hits(11L).build(),
                StatResponse.builder().uri("/events/3").hits(12L).build(),
                StatResponse.builder().uri("/events/4").hits(13L).build()
        );

        Map<Long, Long> expected = Map.of(1L, 10L,
                2L, 11L,
                3L, 12L,
                4L, 13L);

        Map<Long, Long> result = converter.convertEventsStatistics(uriToEventId, stats);
        assertThat(result).isEqualTo(expected);
    }

}