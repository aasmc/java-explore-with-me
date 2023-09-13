package ru.practicum.ewm.service.testutil;


import ru.practicum.ewm.service.events.domain.EventLocation;

import java.time.LocalDateTime;
import java.time.Month;

public class TestConstants {
    public static final String CATEGORY_NAME = "Category Name";

    public static final String EVENT_ANNOTATION = "Event annotation";

    private static final LocalDateTime NOW = LocalDateTime.now();
    public static final LocalDateTime EVENT_CREATED_ON = LocalDateTime
            .of(2023, Month.AUGUST, 30, 12, 12, 12);

    public static final String EVENT_DESCRIPTION = "Event description";
    public static final LocalDateTime EVENT_DATE = LocalDateTime.of(
            NOW.getYear(), NOW.getMonth(), NOW.getDayOfMonth(), NOW.getHour(), NOW.getMinute(), NOW.getSecond()
    );

    public static final float EVENT_LAT = 22.22f;
    public static final float EVENT_LON = 24.24f;
    public static final float NO_EVENT_LAT = 1022.22f;
    public static final float NO_EVENT_LON = 1024.24f;

    public static final float LUZHNIKI_LAT = 55.717721f;
    public static final float LUZHNIKI_LON = 37.554306f;
    public static final float TRETYAKOKVA_LAT = 55.741215f;
    public static final float TRETYAKOKVA_LON = 37.620195f;
    public static final float RADIUS = 7000.0f; // 7 kilometers

    public static final EventLocation TRETYAKOVKA_LOCATION = EventLocation.builder()
            .lat(TRETYAKOKVA_LAT)
            .lon(TRETYAKOKVA_LON)
            .build();
    public static final EventLocation EVENT_LOCATION = EventLocation.builder()
            .lat(EVENT_LAT)
            .lon(EVENT_LON)
            .build();

    public static final EventLocation NO_EVENT_LOCATION = EventLocation.builder()
            .lat(NO_EVENT_LAT)
            .lon(NO_EVENT_LON)
            .build();

    public static final int EVENT_PARTICIPATION_LIMIT = 10;
    public static final LocalDateTime EVENT_PUBLISHED_ON = EVENT_CREATED_ON.plusDays(1);
    public static final LocalDateTime REQUEST_CREATED = EVENT_CREATED_ON.plusDays(1);
    public static final boolean EVENT_REQUEST_MODERATION = false;
    public static final String EVENT_TITLE = "Event Title";
    public static final String USER_NAME = "User Name";
    public static final String USER_EMAIL = "user@email.com";
}
