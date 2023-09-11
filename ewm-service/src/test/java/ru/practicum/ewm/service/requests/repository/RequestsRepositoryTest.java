package ru.practicum.ewm.service.requests.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.service.BaseJpaTest;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.requests.domain.EventStatusCount;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.ewm.service.testutil.TestConstants.*;
import static ru.practicum.ewm.service.testutil.TestData.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestsRepositoryTest extends BaseJpaTest {

    private final RequestsRepository requestsRepository;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;
    private final CategoriesRepository categoriesRepository;

    @Test
    void countByStatus_returnsCorrectList() {
        List<Event> events = saveFiveEvents();
        List<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toList());
        // create 100 requests. 20 requests for each event.
        // all requests are in CONFIRMED state
        save100RequestsForEvents(events, requestsRepository, usersRepository);

        List<EventStatusCount> eventStatusCounts = requestsRepository
                .countByStatus(ParticipationStatus.CONFIRMED, eventIds);

        List<EventStatusCount> expected = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            expected.add(new EventStatusCount(e.getId(), 20L));
        }
        assertThat(eventStatusCounts).isEqualTo(expected);
    }

    private List<Event> saveFiveEvents() {
        List<Event> saved = new ArrayList<>();
        Category category = saveCategory(transientCategory(CATEGORY_NAME), categoriesRepository);
        User user = saveUser(transientUser(USER_EMAIL, USER_NAME), usersRepository);
        for (int i = 0; i < 5; i++) {
            Event event = transientEvent(category, user, false);
            saved.add(eventsRepository.save(event));
        }
        return saved;
    }

}