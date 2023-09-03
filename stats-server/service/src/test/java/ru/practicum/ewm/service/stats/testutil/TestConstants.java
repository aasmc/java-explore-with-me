package ru.practicum.ewm.service.stats.testutil;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;

public class TestConstants {
    public static final String APP = "app";
    public static final String ONE_URL = "/one";
    public static final String TWO_URL_SAME_AS_ONE = "/one";
    public static final String THREE_URL_SAME_AS_ONE = "/one";
    public static final String FOUR_URL = "/four";
    public static final String FIVE_URL_SAME_AS_FOUR = "/four";
    public static final String SIX_URL = "/six";
    public static final String ONE_IP = "0.0.0.1";
    public static final String TWO_IP_SAME_AS_ONE = "0.0.0.1";
    public static final String THREE_IP = "0.0.0.3";
    public static final String FOUR_IP = "0.0.0.4";
    public static final String FIVE_IP_SAVE_AS_FOUR = "0.0.0.4";
    public static final String SIX_IP = "0.0.0.6";
    public static final LocalDateTime ONE_TIMESTAMP = LocalDateTime.of(2023, Month.AUGUST, 2, 12, 1, 1);
    public static final String ONE_TIMESTAMP_STR = "2023-08-02 12:01:01";
    public static final LocalDateTime TWO_TIMESTAMP = ONE_TIMESTAMP.plusDays(1);
    public static final String TWO_TIMESTAMP_STR = "2023-08-03 12:01:01";
    public static final LocalDateTime THREE_TIMESTAMP = ONE_TIMESTAMP.plusDays(2);
    public static final String THREE_TIMESTAMP_STR = "2023-08-04 12:01:01";
    public static final LocalDateTime FOUR_TIMESTAMP = ONE_TIMESTAMP.plusDays(3);
    public static final String FOUR_TIMESTAMP_STR = "2023-08-05 12:01:01";
    public static final LocalDateTime FIVE_TIMESTAMP = ONE_TIMESTAMP.plusDays(4);
    public static final String FIVE_TIMESTAMP_STR = "2023-08-06 12:01:01";
    public static final LocalDateTime SIX_TIMESTAMP = ONE_TIMESTAMP.plusDays(5);
    public static final String SIX_TIMESTAMP_STR = "2023-08-07 12:01:01";

    public static String encodedDateStr(String rawDate) {
        return URLEncoder.encode(rawDate, StandardCharsets.UTF_8);
    }

}
