package ru.practicum.ewm.service;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.practicum.ewm.service.testutil.EwmContainer;

@ActiveProfiles("integtest")
@DataJpaTest
@Testcontainers
@Transactional
@Sql(
        scripts = "classpath:db/clear-db.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class BaseJpaTest {

    @Container
    static PostgreSQLContainer<EwmContainer> container = EwmContainer.getInstance();

}
