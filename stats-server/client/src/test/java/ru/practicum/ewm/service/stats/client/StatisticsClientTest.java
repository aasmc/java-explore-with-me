package ru.practicum.ewm.service.stats.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
class StatisticsClientTest {

    @Autowired
    private StatisticsClient statisticsClient;
    @Autowired
    private ObjectMapper objectMapper;
    public static MockWebServer mockWebServer;

    @BeforeAll
    static void setupServer() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(InetAddress.getByName("localhost"), 9090);
    }

    @AfterAll
    static void tearDownServer() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void getStatistics_correctlySendsRequest_parsesResponse() throws JsonProcessingException, InterruptedException {
        List<StatResponse> responses = List.of(
                new StatResponse("app", "/one", 7L),
                new StatResponse("app", "/two", 6L),
                new StatResponse("app", "/three", 5L)
        );
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(responses))
        );

        ResponseEntity<Object> response = statisticsClient.getStatistics(LocalDateTime.now(), LocalDateTime.now().plusDays(1), List.of("/one", "/two", "three"), true);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Object body = response.getBody();
        String bodyStr = objectMapper.writeValueAsString(body);
        List<StatResponse> result = objectMapper.readValue(bodyStr, new TypeReference<List<StatResponse>>() {});
        assertThat(result.get(0)).isEqualTo(responses.get(0));
        assertThat(result.get(1)).isEqualTo(responses.get(1));
        assertThat(result.get(2)).isEqualTo(responses.get(2));

        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("GET");
        String path = recordedRequest.getPath();
        String patToQuery = path.substring(0, path.indexOf('?'));
        assertThat(patToQuery).isEqualTo("/stats");
    }

    @Test
    void saveStatistics_correctlySendsRequest() throws InterruptedException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(201));
        ResponseEntity<Object> response = statisticsClient.saveStatistics("app", "url", "0.0.0.1", LocalDateTime.now());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertThat(recordedRequest.getMethod()).isEqualTo("POST");
        assertThat(recordedRequest.getPath()).isEqualTo("/hit");
    }

    @Test
    void contextLoads() {

    }

    @SpringBootConfiguration
    public static class Config {

        @Value("${stats-server.url}")
        private String serverUrl;

        @Bean
        public DateUtil dateUtil() {
            return new DateUtil();
        }

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder();
        }

        @Bean
        public StatisticsClient statisticsClient() {
            return new StatisticsClient(serverUrl, restTemplateBuilder(), dateUtil());
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

    }

}