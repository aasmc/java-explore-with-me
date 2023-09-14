package ru.practicum.ewm.service.events.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.service.BaseJpaTest;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.EventLocation;
import ru.practicum.ewm.service.events.domain.EventShort;
import ru.practicum.ewm.service.events.dto.EventSort;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.locations.domain.Coordinates;
import ru.practicum.ewm.service.locations.domain.Location;
import ru.practicum.ewm.service.locations.repository.LocationsRepository;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.ewm.service.testutil.TestConstants.*;
import static ru.practicum.ewm.service.testutil.TestData.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventsRepositoryTest extends BaseJpaTest {

    private final EventsRepository eventsRepository;
    private final CategoriesRepository categoriesRepository;
    private final UsersRepository usersRepository;
    private final LocationsRepository locationsRepository;

    @Test
    void findAllEventsByLocation_whenLocationHasOneEvent_returnsListWithThisEvent() {
        Event event = getOneSavedPublishedEventAtLocation(eventsRepository, usersRepository, categoriesRepository, TRETYAKOVKA_LOCATION);
        Location location = Location.builder().coordinates(Coordinates.builder().radius(RADIUS).lat(LUZHNIKI_LAT).lon(LUZHNIKI_LON).build())
                .name("Luzhniki").build();
        location = locationsRepository.save(location);
        List<EventShort> result = eventsRepository.findAllEventsByLocation(location, "event", null, null, null, null, null, 0, 10);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(event.getId());
    }

    @Test
    void findAllEventsByLocation_whenLocation_hasSomeEvents_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<EventLocation> locationsWithoutEvents = getEqualNLocations(5, NO_EVENT_LOCATION);
        List<EventLocation> locationsWithEvents = getEqualNLocations(5, EVENT_LOCATION);
        List<EventLocation> locations = new ArrayList<>(locationsWithEvents);
        locations.addAll(locationsWithoutEvents);
        getTenSavedPublishedEventsWithLocations(categories, users, eventsRepository, locations);
        Location location = Location.builder().coordinates(Coordinates.builder().radius(100.0f).lon(EVENT_LON).lat(EVENT_LAT).build())
                .name("City").build();
        location = locationsRepository.save(location);
        List<EventShort> result = eventsRepository.findAllEventsByLocation(location, "event", null, null, null, null, null, 0, 10);
        assertThat(result).hasSize(5);
    }

    @Test
    void findAllEventsByLocation_whenLocation_hasNoEvents_returnsEmptyList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<EventLocation> locations = getEqualNLocations(10, EVENT_LOCATION);
        getTenSavedPublishedEventsWithLocations(categories, users, eventsRepository, locations);
        Location location = Location.builder().coordinates(Coordinates.builder().radius(10.0f).lon(NO_EVENT_LON).lat(NO_EVENT_LAT).build())
                .name("City").build();
        location = locationsRepository.save(location);
        List<EventShort> result = eventsRepository.findAllEventsByLocation(location, "event", null, null, null, null, null, 0, 10);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllEventsByLocation_whenLocationHasEvents_returnsListOfEvents() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<EventLocation> locations = getEqualNLocations(10, EVENT_LOCATION);
        List<Event> expected = getTenSavedPublishedEventsWithLocations(categories, users, eventsRepository, locations)
                .stream().sorted(Comparator.comparingLong(Event::getId)).collect(Collectors.toList());
        Location location = Location.builder().coordinates(Coordinates.builder().radius(1000.0f).lon(EVENT_LON).lat(EVENT_LAT).build())
                .name("City").build();
        location = locationsRepository.save(location);

        List<EventShort> result = eventsRepository.findAllEventsByLocation(location, "event", null, null, null, null, null, 0, 10)
                .stream()
                .sorted(Comparator.comparingLong(EventShort::getId)).collect(Collectors.toList());
        assertThat(result).hasSize(10);
        for (int i = 0; i < result.size(); i++) {
            assertThat(result.get(i).getId()).isEqualTo(expected.get(i).getId());
            assertThat(result.get(i).getAnnotation()).isEqualTo(expected.get(i).getAnnotation());
            assertThat(result.get(i).getTitle()).isEqualTo(expected.get(i).getTitle());
        }
    }

    @Test
    void findAllShortEventsBy_whenDBHasUnpublishedEvents_returnsOnlyPublishedEvents() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPublishedEvents(categories,
                users,
                eventsRepository);
        getTenSavedPendingEvents(categories, users, eventsRepository);

        List<EventShort> result = eventsRepository.findAllShortEventsBy("event",
                null,
                false,
                null,
                null,
                EventSort.EVENT_DATE,
                0,
                20);
        assertThat(result).hasSize(10);
        List<EventShort> expected = eventShortListFromEventList(events);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findAllShortEventsBy_whenStartAndEndIsNull_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPublishedEvents(categories,
                users,
                eventsRepository);

        List<EventShort> result = eventsRepository.findAllShortEventsBy("event",
                null,
                false,
                null,
                null,
                EventSort.EVENT_DATE,
                0,
                10);
        assertThat(result).hasSize(10);
        List<EventShort> expected = eventShortListFromEventList(events);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findAllShortEventsBy_whenNoEventSuitsPaid_returnsEmptyList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        getTenSavedPublishedEvents(categories,
                users,
                eventsRepository);

        List<EventShort> result = eventsRepository.findAllShortEventsBy("event",
                null,
                true,
                EVENT_PUBLISHED_ON,
                LocalDateTime.now().plusDays(10),
                EventSort.EVENT_DATE,
                0,
                10);
        assertThat(result).isEmpty();
    }

    @Test
    void findAllShortEventsBy_whenNotAllCategories_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPublishedEvents(categories,
                users,
                eventsRepository);
        List<Long> categoryIds = events.stream()
                .map(Event::getCategory)
                .map(Category::getId)
                .limit(5)
                .collect(Collectors.toList());


        List<EventShort> result = eventsRepository.findAllShortEventsBy("event",
                categoryIds,
                false,
                EVENT_PUBLISHED_ON,
                LocalDateTime.now().plusDays(10),
                EventSort.EVENT_DATE,
                0,
                10);
        assertThat(result).hasSize(5);
        List<EventShort> expected = eventShortListFromEventList(events).subList(0, 5);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findAllShortEventsBy_whenQueryEmpty_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPublishedEvents(categories,
                users,
                eventsRepository);
        List<Long> categoryIds = events.stream()
                .map(Event::getCategory)
                .map(Category::getId)
                .collect(Collectors.toList());


        List<EventShort> result = eventsRepository.findAllShortEventsBy("",
                categoryIds,
                false,
                EVENT_PUBLISHED_ON,
                LocalDateTime.now().plusDays(10),
                EventSort.EVENT_DATE,
                0,
                10);
        assertThat(result).hasSize(10);
        List<EventShort> expected = eventShortListFromEventList(events);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findAllShortEventsBy_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPublishedEvents(categories,
                users,
                eventsRepository);
        List<Long> categoryIds = events.stream()
                .map(Event::getCategory)
                .map(Category::getId)
                .collect(Collectors.toList());


        List<EventShort> result = eventsRepository.findAllShortEventsBy("event",
                categoryIds,
                false,
                EVENT_PUBLISHED_ON,
                LocalDateTime.now().plusDays(10),
                EventSort.EVENT_DATE,
                0,
                10);
        assertThat(result).hasSize(10);
        List<EventShort> expected = eventShortListFromEventList(events);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void findAllEventsBy_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPendingEvents(categories,
                users,
                eventsRepository);
        for (int i = 0; i < 10; i++) { // 10 events will pass by dates
            Event event = events.get(i);
            event.setEventDate(EVENT_DATE.plusDays(i + 1));
            // 10 events will pass by state
            event.setState(EventState.PUBLISHED);
            eventsRepository.saveAndFlush(event);
        }
        LocalDateTime start = EVENT_DATE.plusDays(1);
        LocalDateTime end = EVENT_DATE.plusDays(10);

        List<EventState> states = List.of(EventState.PUBLISHED, EventState.CANCELED);

        List<Long> categoryIds = events.stream()
                .map(Event::getCategory)
                .map(Category::getId)
                .collect(Collectors.toList());

        List<Long> userIds = events.stream()
                .map(Event::getUser)
                .map(User::getId)
                .sorted()
                .collect(Collectors.toList());
        // in total we will have to retrieve 10 events
        List<Event> result = eventsRepository
                .findAllEventsBy(userIds, states, categoryIds, start, end, 0, 10);
        assertThat(result).hasSize(10);
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
    void findAllEventsBy_whenAllParamsNotNull_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPendingEvents(categories,
                users,
                eventsRepository);
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
                .findAllEventsBy(userIds, states, categoryIds, start, end, 0, 10);
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
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPendingEvents(categories,
                users,
                eventsRepository);
        for (int i = 0; i < 5; i++) {
            Event event = events.get(i);
            event.setEventDate(EVENT_DATE.plusDays(i + 1));
            eventsRepository.saveAndFlush(event);
        }
        LocalDateTime start = EVENT_DATE.plusDays(1);
        LocalDateTime end = EVENT_DATE.plusDays(5);
        List<Event> result = eventsRepository
                .findAllEventsBy(null, null, null, start, end, 0, 10)
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
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPendingEvents(categories,
                users,
                eventsRepository);

        List<Long> categoryIds = events.stream()
                .map(Event::getCategory)
                .map(Category::getId)
                .skip(5)
                .limit(5)
                .collect(Collectors.toList());

        List<Event> result = eventsRepository
                .findAllEventsBy(null, null, categoryIds, null, null, 0, 10)
                .stream()
                .sorted(Comparator.comparingLong(e -> e.getCategory().getId()))
                .collect(Collectors.toList());

        assertThat(result).hasSize(5);
        assertThat(result.get(0).getCategory().getId()).isEqualTo(categoryIds.get(0));
        assertThat(result.get(1).getCategory().getId()).isEqualTo(categoryIds.get(1));
        assertThat(result.get(2).getCategory().getId()).isEqualTo(categoryIds.get(2));
        assertThat(result.get(3).getCategory().getId()).isEqualTo(categoryIds.get(3));
        assertThat(result.get(4).getCategory().getId()).isEqualTo(categoryIds.get(4));
    }

    @Test
    void findAllEventsBy_statesNotNull_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPendingEvents(categories,
                users,
                eventsRepository);
        for (int i = 0; i < 5; i++) {
            Event event = events.get(i);
            event.setState(EventState.PUBLISHED);
            eventsRepository.saveAndFlush(event);
        }

        List<EventState> states = List.of(EventState.PENDING, EventState.CANCELED);
        List<Event> result = eventsRepository
                .findAllEventsBy(null, states, null, null, null, 0, 10);

        assertThat(result).hasSize(5);
        for (int i = 0; i < 5; i++) {
            EventState state = result.get(i).getState();
            assertThat(state).isIn(EventState.PENDING, EventState.CANCELED);
        }
    }

    @Test
    void findAllEventsBy_usersNotNull_returnsCorrectList() {
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> events = getTenSavedPendingEvents(categories,
                users,
                eventsRepository);
        List<Long> userIds = events.stream()
                .map(Event::getUser)
                .map(User::getId)
                .skip(5)
                .limit(5)
                .sorted()
                .collect(Collectors.toList());

        List<Event> result = eventsRepository
                .findAllEventsBy(userIds, null, null, null, null, 0, 10)
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
        List<User> users = getTenSavedUsers(usersRepository);
        List<Category> categories = getTenSavedCategories(categoriesRepository);
        List<Event> expected = getTenSavedPendingEvents(categories,
                users,
                eventsRepository);
        List<Event> result = eventsRepository
                .findAllEventsBy(null, null, null, null, null, 0, 10);
        assertThat(result).isEqualTo(expected);
    }


}