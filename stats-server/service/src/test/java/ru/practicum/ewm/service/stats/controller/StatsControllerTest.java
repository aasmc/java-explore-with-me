package ru.practicum.ewm.service.stats.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;
import ru.practicum.ewm.service.stats.storage.StatsRepository;
import ru.practicum.ewm.service.stats.testutil.StatsContainer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.ewm.service.stats.testutil.TestConstants.*;
import static ru.practicum.ewm.service.stats.testutil.TestData.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integtest")
class StatsControllerTest {
    @Container
    static PostgreSQLContainer<StatsContainer> container = StatsContainer.getInstance();

    @Autowired
    private WebTestClient webClient;
    @Autowired
    private StatsRepository statsRepository;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @AfterEach
    void clearDb() {
        statsRepository.deleteAll();
    }

    @Test
    void getStatistics_whenStartAfterNow_throws() {
        LocalDateTime afterNow = LocalDateTime.now().plusDays(10);
        String afterNowStr = afterNow.format(formatter);
        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("start", encodedDateStr(afterNowStr))
                                .queryParam("end", encodedDateStr(SIX_TIMESTAMP_STR))
                                .queryParam("unique", true)
                                .queryParam("uris", ONE_URL, FOUR_URL, SIX_URL)
                                .build()
                )
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getStatistics_whenStartBeforeEnd_throws() {
        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("end", encodedDateStr(ONE_TIMESTAMP_STR))
                                .queryParam("start", encodedDateStr(SIX_TIMESTAMP_STR))
                                .queryParam("unique", true)
                                .queryParam("uris", ONE_URL, FOUR_URL, SIX_URL)
                                .build()
                )
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getStatistics_whenEndNull_throws() {
        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("start", encodedDateStr(ONE_TIMESTAMP_STR))
                                .queryParam("unique", true)
                                .queryParam("uris", ONE_URL, FOUR_URL, SIX_URL)
                                .build()
                )
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getStatistics_whenStartNull_throws() {
        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("end", encodedDateStr(SIX_TIMESTAMP_STR))
                                .queryParam("unique", true)
                                .queryParam("uris", ONE_URL, FOUR_URL, SIX_URL)
                                .build()
                )
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void getStatistics_whenHasStatistics_andHasUrisInRequest_andUnique_returnsCorrectList() {
        saveStatistics();

        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("start", encodedDateStr(ONE_TIMESTAMP_STR))
                                .queryParam("end", encodedDateStr(SIX_TIMESTAMP_STR))
                                .queryParam("unique", true)
                                .queryParam("uris", ONE_URL, FOUR_URL, SIX_URL)
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StatResponse.class).value(stats -> {
                    assertThat(stats).hasSize(3);
                    assertThat(stats.get(0).getHits()).isEqualTo(2);
                    assertThat(stats.get(0).getApp()).isEqualTo(APP);
                    assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);

                    assertThat(stats.get(1).getHits()).isEqualTo(1);
                    assertThat(stats.get(2).getHits()).isEqualTo(1);
                });
    }

    @Test
    void getStatistics_whenHasStatistics_andHasUrisInRequest_returnsCorrectList() {
        saveStatistics();

        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("start", encodedDateStr(ONE_TIMESTAMP_STR))
                                .queryParam("end", encodedDateStr(SIX_TIMESTAMP_STR))
                                .queryParam("uris", ONE_URL, FOUR_URL, SIX_URL)
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StatResponse.class).value(stats -> {
                    assertThat(stats).hasSize(3);
                    assertThat(stats.get(0).getHits()).isEqualTo(3);
                    assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);
                    assertThat(stats.get(1).getHits()).isEqualTo(2);
                    assertThat(stats.get(1).getUri()).isEqualTo(FOUR_URL);
                    assertThat(stats.get(2).getHits()).isEqualTo(1);
                    assertThat(stats.get(2).getUri()).isEqualTo(SIX_URL);
                });
    }

    @Test
    void getStatistics_whenHasStatisticsAndUnique_returnsCorrectList() {
        saveStatistics();

        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("start", encodedDateStr(ONE_TIMESTAMP_STR))
                                .queryParam("end", encodedDateStr(SIX_TIMESTAMP_STR))
                                .queryParam("unique", true)
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StatResponse.class).value(stats -> {
                    assertThat(stats).hasSize(3);
                    assertThat(stats.get(0).getHits()).isEqualTo(2);
                    assertThat(stats.get(0).getApp()).isEqualTo(APP);
                    assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);

                    assertThat(stats.get(1).getHits()).isEqualTo(1);
                    assertThat(stats.get(2).getHits()).isEqualTo(1);
                });
    }

    @Test
    void getStatistics_whenHasStatistics_returnsCorrectList() {
        saveStatistics();

        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("start", encodedDateStr(ONE_TIMESTAMP_STR))
                                .queryParam("end", encodedDateStr(SIX_TIMESTAMP_STR))
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StatResponse.class).value(stats -> {
                    assertThat(stats).hasSize(3);
                    assertThat(stats.get(0).getHits()).isEqualTo(3);
                    assertThat(stats.get(0).getUri()).isEqualTo(ONE_URL);
                    assertThat(stats.get(1).getHits()).isEqualTo(2);
                    assertThat(stats.get(1).getUri()).isEqualTo(FOUR_URL);
                    assertThat(stats.get(2).getHits()).isEqualTo(1);
                    assertThat(stats.get(2).getUri()).isEqualTo(SIX_URL);
                });
    }

    @Test
    void getStatistics_whenNoStatistics_returnsEmptyList() {
        webClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder.path("/stats")
                                .queryParam("start", encodedDateStr(ONE_TIMESTAMP_STR))
                                .queryParam("end", encodedDateStr(SIX_TIMESTAMP_STR))
                                .build()
                )
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(StatResponse.class).value(response -> {
                    assertThat(response).isEmpty();
                });
    }

    @Test
    void addHit_savesStatistics() {
        webClient
                .post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ONE_REQUEST)
                .exchange()
                .expectStatus().isCreated();
    }

    private void saveStatistics() {
        statsRepository.save(ONE);
        statsRepository.save(TWO);
        statsRepository.save(THREE);
        statsRepository.save(FOUR);
        statsRepository.save(FIVE);
        statsRepository.save(SIX);
    }
}