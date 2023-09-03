package ru.practicum.ewm.service.stats.testutil;

import ru.practicum.ewm.service.stats.common.dto.StatRequest;
import ru.practicum.ewm.service.stats.model.Stats;

import static ru.practicum.ewm.service.stats.testutil.TestConstants.*;

public class TestData {
    public static Stats ONE = Stats.builder()
            .app(APP)
            .uri(ONE_URL)
            .ip(ONE_IP)
            .timestamp(ONE_TIMESTAMP)
            .build();

    public static StatRequest ONE_REQUEST = StatRequest.builder()
            .app(APP)
            .uri(ONE_URL)
            .ip(ONE_IP)
            .timestamp(ONE_TIMESTAMP)
            .build();

    public static Stats TWO = Stats.builder()
            .app(APP)
            .uri(TWO_URL_SAME_AS_ONE)
            .ip(TWO_IP_SAME_AS_ONE)
            .timestamp(TWO_TIMESTAMP)
            .build();

    public static StatRequest TWO_REQUEST = StatRequest.builder()
            .app(APP)
            .uri(TWO_URL_SAME_AS_ONE)
            .ip(TWO_IP_SAME_AS_ONE)
            .timestamp(TWO_TIMESTAMP)
            .build();

    public static Stats THREE = Stats.builder()
            .app(APP)
            .uri(THREE_URL_SAME_AS_ONE)
            .ip(THREE_IP)
            .timestamp(THREE_TIMESTAMP)
            .build();

    public static StatRequest THREE_REQUEST = StatRequest.builder()
            .app(APP)
            .uri(THREE_URL_SAME_AS_ONE)
            .ip(THREE_IP)
            .timestamp(THREE_TIMESTAMP)
            .build();

    public static Stats FOUR = Stats.builder()
            .app(APP)
            .uri(FOUR_URL)
            .ip(FOUR_IP)
            .timestamp(FOUR_TIMESTAMP)
            .build();

    public static StatRequest FOUR_REQUEST = StatRequest.builder()
            .app(APP)
            .uri(FOUR_URL)
            .ip(FOUR_IP)
            .timestamp(FOUR_TIMESTAMP)
            .build();

    public static Stats FIVE = Stats.builder()
            .app(APP)
            .uri(FIVE_URL_SAME_AS_FOUR)
            .ip(FIVE_IP_SAVE_AS_FOUR)
            .timestamp(FIVE_TIMESTAMP)
            .build();

    public static StatRequest FIVE_REQUEST = StatRequest.builder()
            .app(APP)
            .uri(FIVE_URL_SAME_AS_FOUR)
            .ip(FIVE_IP_SAVE_AS_FOUR)
            .timestamp(FIVE_TIMESTAMP)
            .build();

    public static Stats SIX = Stats.builder()
            .app(APP)
            .uri(SIX_URL)
            .ip(SIX_IP)
            .timestamp(SIX_TIMESTAMP)
            .build();

    public static StatRequest SIX_REQUEST = StatRequest.builder()
            .app(APP)
            .uri(SIX_URL)
            .ip(SIX_IP)
            .timestamp(SIX_TIMESTAMP)
            .build();
}
