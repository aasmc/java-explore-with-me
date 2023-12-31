package ru.practicum.ewm.service.events.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import ru.practicum.ewm.service.BaseIntegTest;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.events.service.adminservice.AdminEventsService;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;
import ru.practicum.ewm.service.stats.common.util.DateUtil;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;
import static ru.practicum.ewm.service.testutil.TestData.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Rollback(value = false)
class AdminEventsServiceImplTest extends BaseIntegTest {

    // in test we create 10 events and for each event we create 10 confirmed requests
    private static final Long CONFIRMED_EVENTS_COUNT = 10L;
    private static final Long EVENT_VIEWS = 100L;

    private final AdminEventsService adminService;
    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;
    private final DateUtil dateUtil;
    @MockBean
    private StatisticsService statisticsService;

    @Test
    void getAllEvents_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Long> userIds = users.stream().map(User::getId).sorted().collect(Collectors.toList());
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Long> categoryIds = categories.stream().map(Category::getId).sorted().collect(Collectors.toList());
        List<Event> events = getTenSavedPublishedEvents(categories, users, eventsRepository);
        List<Long> eventIds = events.stream().map(Event::getId).sorted().collect(Collectors.toList());
        save100RequestsForEvents(events, requestsRepository, usersRepository);
        LocalDateTime start = events.stream().map(Event::getEventDate)
                .min(Comparator.naturalOrder()).orElse(LocalDateTime.now());
        Map<Long, Long> eventIdToViews = new HashMap<>();
        events.forEach(e -> {
            eventIdToViews.put(e.getId(), EVENT_VIEWS);
        });
        List<EventState> states = new ArrayList<>();
        states.add(EventState.PUBLISHED);

        LocalDateTime end = dateUtil.getDefaultDate();

        when(statisticsService.getEventsViews(eventIds, start, end, false))
                .thenReturn(eventIdToViews);
        List<EventFullDto> expected = new ArrayList<>();
        for (Event e : events) {
            expected.add(eventFullDtoFromEvent(e, CONFIRMED_EVENTS_COUNT, EVENT_VIEWS));
        }

        List<EventFullDto> result = adminService.getAllEvents(userIds,
                states,
                categoryIds,
                start,
                end,
                0, 10);
        for (int i = 0; i < result.size(); i++) {
            EventFullDto actualDto = result.get(i);
            EventFullDto expectedDto = expected.get(i);
            compareEventFullDtos(actualDto, expectedDto);
        }
    }

    private void compareEventFullDtos(EventFullDto actual, EventFullDto expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getAnnotation()).isEqualTo(expected.getAnnotation());
        assertThat(actual.getCategory()).isEqualTo(expected.getCategory());
        assertThat(actual.getConfirmedRequests()).isEqualTo(expected.getConfirmedRequests());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getInitiator()).isEqualTo(expected.getInitiator());
        assertThat(actual.getLocation()).isEqualTo(expected.getLocation());
        assertThat(actual.isPaid()).isEqualTo(expected.isPaid());
        assertThat(actual.getParticipantLimit()).isEqualTo(expected.getParticipantLimit());
        assertThat(actual.isRequestModeration()).isEqualTo(expected.isRequestModeration());
        assertThat(actual.getState()).isEqualTo(expected.getState());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getViews()).isEqualTo(expected.getViews());
    }

}