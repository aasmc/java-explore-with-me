package ru.practicum.ewm.service;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("integtest")
@DataJpaTest
@Testcontainers
@Transactional
@Sql(
        scripts = "classpath:database/clear-db.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseJpaTest {


}
