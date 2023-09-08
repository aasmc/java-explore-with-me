package ru.practicum.ewm.service.testutil;

import org.testcontainers.containers.PostgreSQLContainer;

public class EwmContainer extends PostgreSQLContainer<EwmContainer> {

    private static final String IMAGE_VERSION = "postgres:14-alpine";

    private static EwmContainer container;

    private EwmContainer() {
        super(IMAGE_VERSION);
    }

    public static EwmContainer getInstance() {
        if (container == null) {
            container = new EwmContainer();
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
