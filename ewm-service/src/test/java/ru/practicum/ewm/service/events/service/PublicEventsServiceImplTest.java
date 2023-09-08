package ru.practicum.ewm.service.events.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.ewm.service.BaseIntegTest;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.EventSort;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static ru.practicum.ewm.service.testutil.TestData.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class PublicEventsServiceImplTest extends BaseIntegTest {
    // in test we create 10 events and for each event we create 10 confirmed requests
    private static final Long CONFIRMED_EVENTS_COUNT = 10L;
    private static final Long EVENT_VIEWS = 100L;
    private final PublicEventsService publicService;
    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;
    @MockBean
    private StatisticsService statisticsService;

    @Test
    void getAllEvents_whenThereAreAvailableEvents_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Long> categoryIds = categories.stream().map(Category::getId).sorted().collect(Collectors.toList());
        List<Event> events = getTenSavedPublishedEvents(categories, users, eventsRepository, 11);
        List<Long> eventIds = events.stream().map(Event::getId).sorted().collect(Collectors.toList());
        save100RequestsForEvents(events, requestsRepository, usersRepository);

        LocalDateTime start = events.stream().map(Event::getPublishedOn)
                .min(Comparator.naturalOrder()).orElse(LocalDateTime.now());
        LocalDateTime end = events.stream().map(Event::getEventDate)
                .max(Comparator.naturalOrder()).orElse(LocalDateTime.now());

        Map<Long, Long> eventIdToViews = new HashMap<>();
        events.forEach(e -> {
            eventIdToViews.put(e.getId(), EVENT_VIEWS);
        });
        when(statisticsService.getEventsViews(eventIds, start, end, false))
                .thenReturn(eventIdToViews);

        List<EventShortDto> result = publicService.getAllEvents("event",
                categoryIds,
                false,
                start,
                end,
                true,
                EventSort.EVENT_DATE,
                0,
                10);
        assertThat(result).hasSize(events.size());
        List<EventShortDto> expected = eventShortDtoListFromEventList(events, CONFIRMED_EVENTS_COUNT, EVENT_VIEWS);
        assertThat(result).isEqualTo(expected);

    }

    @Test
    void getAllEvents_whenAllEventsAreUnavailable_returnsEmptyList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Long> categoryIds = categories.stream().map(Category::getId).sorted().collect(Collectors.toList());
        List<Event> events = getTenSavedPublishedEvents(categories, users, eventsRepository);
        List<Long> eventIds = events.stream().map(Event::getId).sorted().collect(Collectors.toList());
        save100RequestsForEvents(events, requestsRepository, usersRepository);

        LocalDateTime start = events.stream().map(Event::getPublishedOn)
                .min(Comparator.naturalOrder()).orElse(LocalDateTime.now());
        LocalDateTime end = events.stream().map(Event::getEventDate)
                .max(Comparator.naturalOrder()).orElse(LocalDateTime.now());

        Map<Long, Long> eventIdToViews = new HashMap<>();
        events.forEach(e -> {
            eventIdToViews.put(e.getId(), EVENT_VIEWS);
        });
        when(statisticsService.getEventsViews(eventIds, start, end, false))
                .thenReturn(eventIdToViews);

        List<EventShortDto> result = publicService.getAllEvents("event",
                categoryIds,
                false,
                start,
                end,
                true,
                EventSort.EVENT_DATE,
                0,
                10);
        assertThat(result).isEmpty();
    }
}