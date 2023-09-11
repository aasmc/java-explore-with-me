package ru.practicum.ewm.service.events.util;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class EventUriConverterTest {

    private final EventUriConverter converter = new EventUriConverter();

    @Test
    void convertEventIdsToUris_returnsCorrectList() {
        List<Long> ids = List.of(1L, 2L, 3L, 4L);
        Map<String, Long> expected = Map.of("/events/1", 1L,
                "/events/2", 2L,
                "/events/3", 3L,
                "/events/4", 4L
        );
        Map<String, Long> result = converter.convertEventIdsToUris(ids);
        assertThat(result).isEqualTo(expected);
    }

}