package ru.practicum.ewm.service.events.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class EventUriConverter {

    private static final String EVENT_BASE_URL = "/events/";

    public Map<String, Long> convertEventIdsToUris(List<Long> ids) {
        return ids.stream()
                .collect(Collectors.toMap(id -> EVENT_BASE_URL + id,
                        Function.identity(),
                        (l, r) -> l,
                        HashMap::new));
    }

}
