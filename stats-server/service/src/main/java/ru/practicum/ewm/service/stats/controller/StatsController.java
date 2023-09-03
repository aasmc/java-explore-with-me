package ru.practicum.ewm.service.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.stats.common.dto.StatRequest;
import ru.practicum.ewm.service.stats.common.dto.StatResponse;
import ru.practicum.ewm.service.stats.common.util.DateUtil;
import ru.practicum.ewm.service.stats.error.StatsServiceException;
import ru.practicum.ewm.service.stats.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;
    private final DateUtil dateUtil;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody @Valid StatRequest request) {
        log.info("Received POST request to add stat hit: {}", request);
        statsService.saveStat(request);
    }

    @GetMapping("/stats")
    public List<StatResponse> getStatistics(@RequestParam("start") String start,
                                            @RequestParam("end") String end,
                                            @RequestParam(value = "uris", required = false) List<String> uris,
                                            @RequestParam(value = "unique", required = false, defaultValue = "false") boolean unique) {
        log.info("Received request to GET statistics for period start: {}, end: {}", start, end);
        LocalDateTime startDate = dateUtil.decodeFromString(start);
        LocalDateTime endDate = dateUtil.decodeFromString(end);
        checkDatesCorrect(startDate, endDate);
        List<StatResponse> statistics = statsService.getStatistics(startDate, endDate, uris, unique);
        log.info("Retrieved statistics: {}", statistics);
        return statistics;
    }

    /**
     * Checks if start date is before now(), since it is incorrect to request
     * statistics for period that has not yet started. Also checks that
     * start date is before end date to ensure period validity.
     * If any of the above checks fails, the method throws a StatsServiceException,
     * which is a RuntimeException, handled by DefaultErrorHandler.
     */
    private void checkDatesCorrect(LocalDateTime start, LocalDateTime end) {
        if (start.isAfter(LocalDateTime.now()) || start.isAfter(end)) {
            String msg = String.format("Invalid date parameters: %s, %s",
                    start.format(DateUtil.FORMATTER),
                    end.format(DateUtil.FORMATTER));
            throw new StatsServiceException(HttpStatus.BAD_REQUEST.value(), msg);
        }
    }
}
