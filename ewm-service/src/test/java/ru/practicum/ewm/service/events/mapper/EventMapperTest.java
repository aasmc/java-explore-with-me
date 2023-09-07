package ru.practicum.ewm.service.events.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.ewm.service.categories.mapper.CategoriesMapper;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.usermanagement.mapper.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.ewm.service.util.TestData.eventFullDtoFromEvent;
import static ru.practicum.ewm.service.util.TestData.transientEvent;

class EventMapperTest {

    private CategoriesMapper categoriesMapper;
    private UserMapper userMapper;
    private EventMapper eventMapper;

    @BeforeEach
    void setup() {
        categoriesMapper = new CategoriesMapper();
        userMapper = new UserMapper();
        eventMapper = new EventMapper(categoriesMapper, userMapper);
    }

    @Test
    void mapToFullDtoList_mapsCorrectly() {
        Event eventOne = transientEvent(1L, 1L, false, "Cat", "User", "user@email.com");
        eventOne.setId(1L);
        Event eventTwo = transientEvent(2L, 2L, false, "Cat2", "User2", "user2@email.com");
        eventTwo.setId(2L);
        Long confirmedOne = 1000L;
        Long viewsOne = 1000L;
        Long confirmedTwo = 2000L;
        Long viewsTwo = 2000L;
        EventFullDto expectedOne = eventFullDtoFromEvent(eventOne, confirmedOne, viewsOne);
        EventFullDto expectedTwo = eventFullDtoFromEvent(eventTwo, confirmedTwo, viewsTwo);
        List<EventFullDto> expected = List.of(expectedOne, expectedTwo);

        Map<Long, Long> eventIdToViews = new HashMap<>();
        eventIdToViews.put(1L, viewsOne);
        eventIdToViews.put(2L, viewsTwo);

        Map<Long, Long> confirmedEventIdToCount = new HashMap<>();
        confirmedEventIdToCount.put(1L, confirmedOne);
        confirmedEventIdToCount.put(2L, confirmedTwo);

        List<EventFullDto> result = eventMapper
                .mapToFullDtoList(List.of(eventOne, eventTwo), eventIdToViews, confirmedEventIdToCount);
        for (int i = 0; i < result.size(); i++) {
            EventFullDto res = result.get(i);
            EventFullDto ex = expected.get(i);
            assertThat(res).isEqualTo(ex);
        }
    }

    @Test
    void mapToFullDto_mapsCorrectly() {
        Event event = transientEvent(1L, 1L, false, "Cat", "User", "user@email.com");
        event.setId(1L);
        Long confirmed = 100L;
        Long views = 1000L;

        EventFullDto expected = eventFullDtoFromEvent(event, confirmed, views);

        EventFullDto result = eventMapper.mapToFullDto(event, confirmed, views);
        assertThat(result).isEqualTo(expected);
    }

}