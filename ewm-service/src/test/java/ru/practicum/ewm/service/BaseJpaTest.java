package ru.practicum.ewm.service;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.practicum.ewm.service.util.EwmContainer;

@ActiveProfiles("integtest")
@DataJpaTest
@Testcontainers
@Transactional
public class BaseJpaTest {

    @Container
    static PostgreSQLContainer<EwmContainer> container = EwmContainer.getInstance();

}
