package ru.practicum.ewm.service.util;

import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.dto.NewCategoryDto;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.dto.NewUserRequest;

import static ru.practicum.ewm.service.util.TestConstants.*;

public class TestData {
    public static NewCategoryDto newCategoryDto(String name) {
        return NewCategoryDto.builder().name(CATEGORY_NAME).build();
    }

    public static CategoryDto fromNewCategoryDto(NewCategoryDto dto) {
        return CategoryDto.builder().name(dto.getName()).build();
    }

    public static Category transientCategory(String name) {
        return Category.builder().name(name).build();
    }

    public static User transientUser(String email, String name) {
        return User.builder().email(email).name(name).build();
    }

    public static NewUserRequest newUserRequest(String email, String name) {
        return NewUserRequest.builder()
                .name(name)
                .email(email)
                .build();
    }

    public static NewUserRequest newUserRequestFromUser(User user) {
        return NewUserRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static Category category(Long catId, String catName) {
        return Category.builder().id(catId).name(catName).build();
    }

    public static User user(Long userId, String email, String name) {
        return User.builder().id(userId).email(email).name(name).build();
    }

    public static Event transientEvent(Long catId,
                                       Long userId,
                                       boolean paid,
                                       String catName,
                                       String userName,
                                       String userEmail) {
        return Event.builder()
                .annotation(EVENT_ANNOTATION)
                .category(category(catId, catName))
                .createdOn(EVENT_CREATED_ON)
                .description(EVENT_DESCRIPTION)
                .eventDate(EVENT_DATE)
                .user(user(userId, userEmail, userName))
                .location(EVENT_LOCATION)
                .paid(paid)
                .participationLimit(EVENT_PARTICIPATION_LIMIT)
                .publishedOn(null)
                .requestModeration(EVENT_REQUEST_MODERATION)
                .state(EventState.PENDING)
                .title(EVENT_TITLE)
                .build();
    }
}
