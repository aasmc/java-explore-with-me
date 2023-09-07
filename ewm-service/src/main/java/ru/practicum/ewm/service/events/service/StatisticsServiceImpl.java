package ru.practicum.ewm.service.events.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.error.ErrorConstants;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.util.EventUriConverter;
import ru.practicum.ewm.service.events.util.StatisticsConverter;
import ru.practicum.ewm.service.stats.client.StatisticsClient;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ObjectMapper objectMapper;
    private final StatisticsClient statisticsClient;
    private final EventUriConverter uriConverter;
    private final StatisticsConverter statisticsConverter;

    @Override
    public List<StatResponse> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        ResponseEntity<Object> response = statisticsClient.getStatistics(start, end, uris, unique);
        Object body = response.getBody();
        try {
            String bodyStr = objectMapper.writeValueAsString(body);
            List<StatResponse> statistics = objectMapper.readValue(bodyStr, new TypeReference<>() {});
            log.info("Retrieved statistics from Stat Server: {}", statistics);
            return statistics;
        } catch (JsonProcessingException e) {
            String msg = "Failed to parse response from Statistics Client";
            throw new EwmServiceException(msg,
                    ErrorConstants.FAILED_STATISTICS_REASON,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    LocalDateTime.now());
        }
    }

    @Override
    public void saveStatistics(String app, String uri, String ip, LocalDateTime timestamp) {
        statisticsClient.saveStatistics(app, uri, ip, timestamp);
    }

    @Override
    public Map<Long, Long> getEventsViews(List<Long> eventIds,
                                          LocalDateTime start,
                                          LocalDateTime end,
                                          boolean unique) {
        Map<String, Long> uriToEventId = uriConverter.convertEventIdsToUris(eventIds);
        List<StatResponse> statistics = getStatistics(start, end, List.copyOf(uriToEventId.keySet()), null);
        return statisticsConverter.convertEventsStatistics(uriToEventId, statistics);
    }
}
