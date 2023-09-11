package ru.practicum.ewm.service.events.service.statisticsservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.error.ErrorConstants;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.util.EventUriConverter;
import ru.practicum.ewm.service.events.util.StatisticsConverter;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;
import ru.practicum.ewm.service.stats.client.StatisticsClient;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final ObjectMapper objectMapper;
    private final StatisticsClient statisticsClient;
    private final EventUriConverter uriConverter;
    private final StatisticsConverter statisticsConverter;
    private final RequestsRepository requestsRepository;

    @Override
    public List<StatResponse> getStatistics(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        try {
            ResponseEntity<Object> response = statisticsClient.getStatistics(start, end, uris, unique);
            Object body = response.getBody();
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
        List<StatResponse> statistics = getStatistics(start, end, List.copyOf(uriToEventId.keySet()), unique);
        return statisticsConverter.convertEventsStatistics(uriToEventId, statistics);
    }

    @Override
    public Map<Long, Long> getConfirmedCount(List<Long> eventsIds) {
        return requestsRepository.getEventIdCountByParticipationStatus(ParticipationStatus.CONFIRMED, eventsIds);
    }

    @Override
    public Long getConfirmedCountForEvent(Long eventId) {
        Map<Long, Long> confirmedCount = getConfirmedCount(List.of(eventId));
        return confirmedCount.getOrDefault(eventId, 0L);
    }

    @Override
    public Long getEventViews(Long eventId, LocalDateTime start, LocalDateTime end, boolean unique) {
        Map<Long, Long> eventsViews = getEventsViews(List.of(eventId), start, end, unique);
        return eventsViews.getOrDefault(eventId, 0L);
    }
}
