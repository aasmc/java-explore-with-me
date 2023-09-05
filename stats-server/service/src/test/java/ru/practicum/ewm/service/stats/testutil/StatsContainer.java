package ru.practicum.ewm.service.stats.testutil;

import org.testcontainers.containers.PostgreSQLContainer;

public class StatsContainer extends PostgreSQLContainer<StatsContainer> {

    private static final String IMAGE_VERSION = "postgres:14-alpine";

    private static StatsContainer container;

    private StatsContainer() {
        super(IMAGE_VERSION);
    }

    public static StatsContainer getInstance() {
        if (container == null) {
            container = new StatsContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", container.getJdbcUrl());
        System.setProperty("DB_USERNAME", container.getUsername());
        System.setProperty("DB_PASSWORD", container.getPassword());
    }

    @Override
    public void stop() {
        // do nothing, JVM handles shut down
    }
}
