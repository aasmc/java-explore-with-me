package ru.practicum.ewm.service.util;

import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.dto.NewCategoryDto;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.Location;
import ru.practicum.ewm.service.events.dto.*;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.dto.NewUserRequest;
import ru.practicum.ewm.service.usermanagement.dto.UserShortDto;

import static ru.practicum.ewm.service.events.dto.AdminEventStateAction.*;
import static ru.practicum.ewm.service.events.dto.EventState.*;
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

    public static Event defaultPendingEvent() {
        return transientEvent(1L, 1L, false, "Cat", "User", "user@email.com");
    }

    public static UpdateEventAdminRequest updateEventAdminRequest() {
        return UpdateEventAdminRequest.builder()
                .annotation("New Annotation")
                .category(1L)
                .description("New Description")
                .eventDate(EVENT_DATE.plusDays(1))
                .location(LocationDto.builder().lat(4.4f).lon(5.5f).build())
                .paid(false)
                .participantLimit(10)
                .requestModeration(false)
                .stateAction(PUBLISH_EVENT)
                .title("New Title")
                .build();
    }

    public static Event eventFromUpdateRequest(UpdateEventAdminRequest request,
                                          Long eventId,
                                          User user,
                                          Category category) {
        return Event.builder()
                .id(eventId)
                .annotation(request.getAnnotation())
                .category(category)
                .createdOn(EVENT_CREATED_ON)
                .description(request.getDescription())
                .eventDate(request.getEventDate())
                .user(user)
                .location(Location.builder()
                        .lat(request.getLocation().getLat())
                        .lon(request.getLocation().getLon())
                        .build())
                .paid(request.getPaid())
                .participationLimit(request.getParticipantLimit())
                .publishedOn(null)
                .requestModeration(request.getRequestModeration())
                .state(request.getStateAction() == REJECT_EVENT ? CANCELED : PUBLISHED)
                .title(request.getTitle())
                .build();
    }

    public static Event transientEvent(Category category, User user, boolean paid) {
        return Event.builder()
                .annotation(EVENT_ANNOTATION)
                .category(category)
                .createdOn(EVENT_CREATED_ON)
                .description(EVENT_DESCRIPTION)
                .eventDate(EVENT_DATE)
                .user(user)
                .location(EVENT_LOCATION)
                .paid(paid)
                .participationLimit(EVENT_PARTICIPATION_LIMIT)
                .publishedOn(null)
                .requestModeration(EVENT_REQUEST_MODERATION)
                .state(PENDING)
                .title(EVENT_TITLE)
                .build();
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
                .state(PENDING)
                .title(EVENT_TITLE)
                .build();
    }

    public static Request transientRequest(Event event, User requester, ParticipationStatus status) {
        return Request.builder()
                .created(REQUEST_CREATED)
                .event(event)
                .requester(requester)
                .status(status)
                .build();
    }

    public static EventFullDto eventFullDtoFromEvent(Event event, Long confirmed, Long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryDto.builder().id(event.getCategory().getId()).name(event.getCategory().getName()).build())
                .confirmedRequests(confirmed)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserShortDto.builder().id(event.getUser().getId()).name(event.getUser().getName()).build())
                .location(LocationDto.builder()
                        .lat(event.getLocation().getLat())
                        .lon(event.getLocation().getLon())
                        .build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipationLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }
}
