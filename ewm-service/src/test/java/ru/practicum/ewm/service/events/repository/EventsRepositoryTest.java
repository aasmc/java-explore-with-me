package ru.practicum.ewm.service.events.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.service.BaseJpaTest;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;
import ru.practicum.ewm.service.util.OffsetBasedPageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.ewm.service.util.TestConstants.*;
import static ru.practicum.ewm.service.util.TestData.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventsRepositoryTest extends BaseJpaTest {

    private final EventsRepository eventsRepository;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;

    @BeforeEach
    void clearDb() {
        categoriesRepository.deleteAll();
        usersRepository.deleteAll();
        eventsRepository.deleteAll();
    }

    @Test
    void findAllEventsBy_whenAllParamsNotNull_returnsCorrectList() {
        List<User> users = getTenTransientUsers();
        List<Category> categories = getTenTransientCategories();
        List<Event> events = saveTenEvents(categories, users);
        for (int i = 0; i < 5; i++) { // first 5 events will pass by dates
            Event event = events.get(i);
            event.setEventDate(EVENT_DATE.plusDays(i + 1));
            // first 3 events will pass by state
            if (i <= 3) {
                event.setState(EventState.PUBLISHED);
            }
            eventsRepository.saveAndFlush(event);
        }
        LocalDateTime start = EVENT_DATE.plusDays(1);
        LocalDateTime end = EVENT_DATE.plusDays(5);

        List<EventState> states = List.of(EventState.PUBLISHED, EventState.CANCELED);

        List<Long> categoryIds = events.stream()
                .map(Event::getCategory)
                .map(Category::getId)
                .limit(4) // first 4 events will pass by categoryId
                .collect(Collectors.toList());

        List<Long> userIds = events.stream()
                .map(Event::getUser)
                .map(User::getId)
                .limit(2) // first 2 events will pass by userId
                .sorted()
                .collect(Collectors.toList());
        // in total we will have to retrieve 2 events
        List<Event> result = eventsRepository
                .findAllEventsBy(userIds, states, categoryIds, start, end, new OffsetBasedPageRequest(0, 10));
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUser().getId()).isEqualTo(userIds.get(0));
        assertThat(result.get(0).getCategory().getId()).isEqualTo(categoryIds.get(0));
        assertThat(result.get(0).getEventDate()).isEqualTo(EVENT_DATE.plusDays(1));
        assertThat(result.get(0).getState()).isEqualTo(EventState.PUBLISHED);

        assertThat(result.get(1).getUser().getId()).isEqualTo(userIds.get(1));
        assertThat(result.get(1).getCategory().getId()).isEqualTo(categoryIds.get(1));
        assertThat(result.get(1).getEventDate()).isEqualTo(EVENT_DATE.plusDays(2));
        assertThat(result.get(1).getState()).isEqualTo(EventState.PUBLISHED);
    }

    @Test
    void findAllEventsBy_startAndEndNotNull_returnsCorrectList() {
        List<User> users = getTenTransientUsers();
        List<Category> categories = getTenTransientCategories();
        List<Event> events = saveTenEvents(categories, users);
        for (int i = 0; i < 5; i++) {
            Event event = events.get(i);
            event.setEventDate(EVENT_DATE.plusDays(i + 1));
            eventsRepository.saveAndFlush(event);
        }
        LocalDateTime start = EVENT_DATE.plusDays(1);
        LocalDateTime end = EVENT_DATE.plusDays(5);
        List<Event> result = eventsRepository
                .findAllEventsBy(null, null, null, start, end, new OffsetBasedPageRequest(0, 10))
                .stream()
                .sorted(Comparator.comparing(Event::getEventDate))
                .collect(Collectors.toList());

        assertThat(result).hasSize(5);

        for (int i = 0; i < 5; i++) {
            Event event = result.get(i);
            LocalDateTime expected = EVENT_DATE.plusDays(i + 1);
            assertThat(event.getEventDate()).isEqualTo(expected);
        }
    }

    @Test
    void findAllEventsBy_categoriesNotNull_returnsCorrectList() {
        List<User> users = getTenTransientUsers();
        List<Category> categories = getTenTransientCategories();
        List<Event> events = saveTenEvents(categories, users);

        List<Long> categoryIds = events.stream()
                .map(Event::getCategory)
                .map(Category::getId)
                .skip(5)
                .limit(5)
                .collect(Collectors.toList());

        List<Event> result = eventsRepository
                .findAllEventsBy(null, null, categoryIds, null, null, new OffsetBasedPageRequest(0, 10))
                .stream()
                .sorted(Comparator.comparingLong(e -> e.getCategory().getId()))
                .collect(Collectors.toList());

        assertThat(result).hasSize(5);
        assertThat(result.get(0).getUser().getId()).isEqualTo(categoryIds.get(0));
        assertThat(result.get(1).getUser().getId()).isEqualTo(categoryIds.get(1));
        assertThat(result.get(2).getUser().getId()).isEqualTo(categoryIds.get(2));
        assertThat(result.get(3).getUser().getId()).isEqualTo(categoryIds.get(3));
        assertThat(result.get(4).getUser().getId()).isEqualTo(categoryIds.get(4));
    }

    @Test
    void findAllEventsBy_statesNotNull_returnsCorrectList() {
        List<User> users = getTenTransientUsers();
        List<Category> categories = getTenTransientCategories();
        List<Event> events = saveTenEvents(categories, users);
        for (int i = 0; i < 5; i++) {
            Event event = events.get(i);
            event.setState(EventState.PUBLISHED);
            eventsRepository.saveAndFlush(event);
        }

        List<EventState> states = List.of(EventState.PENDING, EventState.CANCELED);
        List<Event> result = eventsRepository
                .findAllEventsBy(null, states, null, null, null, new OffsetBasedPageRequest(0, 10));

        assertThat(result).hasSize(5);
        for (int i = 0; i < 5; i++) {
            EventState state = result.get(i).getState();
            assertThat(state).isIn(EventState.PENDING, EventState.CANCELED);
        }
    }

    @Test
    void findAllEventsBy_usersNotNull_returnsCorrectList() {
        List<User> users = getTenTransientUsers();

        List<Category> categories = getTenTransientCategories();
        List<Event> events = saveTenEvents(categories, users);
        List<Long> userIds = events.stream()
                .map(Event::getUser)
                .map(User::getId)
                .skip(5)
                .limit(5)
                .sorted()
                .collect(Collectors.toList());

        List<Event> result = eventsRepository
                .findAllEventsBy(userIds, null, null, null, null, new OffsetBasedPageRequest(0, 10))
                .stream()
                .sorted(Comparator.comparingLong(e -> e.getUser().getId()))
                .collect(Collectors.toList());

        assertThat(result).hasSize(5);
        assertThat(result.get(0).getUser().getId()).isEqualTo(userIds.get(0));
        assertThat(result.get(1).getUser().getId()).isEqualTo(userIds.get(1));
        assertThat(result.get(2).getUser().getId()).isEqualTo(userIds.get(2));
        assertThat(result.get(3).getUser().getId()).isEqualTo(userIds.get(3));
        assertThat(result.get(4).getUser().getId()).isEqualTo(userIds.get(4));
    }

    @Test
    void findAllEventsBy_allParamsNull_returnsAllEvents() {
        List<User> users = getTenTransientUsers();
        List<Category> categories = getTenTransientCategories();
        List<Event> expected = saveTenEvents(categories, users);
        List<Event> result = eventsRepository
                .findAllEventsBy(null, null, null, null, null, new OffsetBasedPageRequest(0, 10));
        assertThat(result).isEqualTo(expected);
    }

    private List<User> getTenTransientUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(transientUser(USER_EMAIL + i, USER_NAME + i));
        }
        return users;
    }

    private List<Category> getTenTransientCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            categories.add(transientCategory(CATEGORY_NAME + i));
        }
        return categories;
    }

    private List<Event> saveTenEvents(List<Category> categories, List<User> users) {
        List<Event> saved = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = saveCategory(categories.get(i));
            User user = saveUser(users.get(i));
            Event event = transientEvent(category, user, false);
            saved.add(eventsRepository.save(event));
        }
        return saved;
    }

    private Category saveCategory(Category category) {
        return categoriesRepository.save(category);
    }

    private User saveUser(User user) {
        return usersRepository.save(user);
    }

}