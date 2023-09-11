package ru.practicum.ewm.service.events.util;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StatisticsConverter {

    public Map<Long, Long> convertEventsStatistics(Map<String, Long> uriToEventId,
                                                   List<StatResponse> stats) {
        return stats.stream()
                .collect(Collectors.toMap(response -> {
                            String uri = response.getUri();
                            return uriToEventId.get(uri);
                        },
                        StatResponse::getHits,
                        Long::sum,
                        HashMap::new
                ));
    }

}
