package ru.practicum.ewm.service.testutil;

import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.dto.NewCategoryDto;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.domain.EventLocation;
import ru.practicum.ewm.service.events.domain.EventLocation_;
import ru.practicum.ewm.service.events.domain.EventShort;
import ru.practicum.ewm.service.events.dto.EventFullDto;
import ru.practicum.ewm.service.events.dto.EventShortDto;
import ru.practicum.ewm.service.events.dto.EventLocationDto;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.dto.NewUserRequest;
import ru.practicum.ewm.service.usermanagement.dto.UserShortDto;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.service.events.dto.AdminEventStateAction.PUBLISH_EVENT;
import static ru.practicum.ewm.service.events.dto.AdminEventStateAction.REJECT_EVENT;
import static ru.practicum.ewm.service.events.dto.EventState.*;
import static ru.practicum.ewm.service.testutil.TestConstants.*;

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

    public static UpdateEventAdminRequest updateEventAdminRequestPublishAction() {
        return UpdateEventAdminRequest.builder()
                .annotation("New Annotation")
                .category(1L)
                .description("New Description")
                .eventDate(EVENT_DATE.plusDays(1))
                .location(EventLocationDto.builder().lat(4.4f).lon(5.5f).build())
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
                .location(EventLocation.builder()
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

    public static Event transientEventWithLocation(Category category, User user, boolean paid, EventLocation location) {
        Event event = transientEvent(category, user, paid);
        event.setLocation(location);
        return event;
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
                .location(EventLocationDto.builder()
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

    public static List<User> getTenTransientUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            users.add(transientUser(USER_EMAIL + i, USER_NAME + i));
        }
        return users;
    }

    public static List<Category> getTenTransientCategories() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            categories.add(transientCategory(CATEGORY_NAME + i));
        }
        return categories;
    }

    public static List<Event> getTenSavedPendingEvents(List<Category> savedCategories,
                                                       List<User> savedUsers,
                                                       EventsRepository eventsRepository) {
        List<Event> saved = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = savedCategories.get(i);
            User user = savedUsers.get(i);
            Event event = transientEvent(category, user, false);
            saved.add(eventsRepository.save(event));
        }
        return saved;
    }

    public static List<Event> getTenSavedPublishedEvents(List<Category> savedCategories,
                                                         List<User> savedUsers,
                                                         EventsRepository eventsRepository) {
        List<Event> saved = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = savedCategories.get(i);
            User user = savedUsers.get(i);
            Event event = transientEvent(category, user, false);
            event.setState(PUBLISHED);
            event.setEventDate(EVENT_DATE.plusDays(i + 1));
            event.setPublishedOn(EVENT_PUBLISHED_ON);
            saved.add(eventsRepository.save(event));
        }
        return saved;
    }

    public static List<Event> getTenSavedPublishedEventsWithLocations(List<Category> savedCategories,
                                                                      List<User> savedUsers,
                                                                      EventsRepository eventsRepository,
                                                                      List<EventLocation> locations) {
        List<Event> saved = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = savedCategories.get(i);
            User user = savedUsers.get(i);
            EventLocation location = locations.get(i);
            Event event = transientEventWithLocation(category, user, false, location);
            event.setState(PUBLISHED);
            event.setEventDate(EVENT_DATE.plusDays(i + 1));
            event.setPublishedOn(EVENT_PUBLISHED_ON);
            saved.add(eventsRepository.save(event));
        }
        return saved;
    }

    public static List<Event> getTenSavedPublishedEvents(List<Category> savedCategories,
                                                         List<User> savedUsers,
                                                         EventsRepository eventsRepository,
                                                         int participationLimit) {
        List<Event> saved = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Category category = savedCategories.get(i);
            User user = savedUsers.get(i);
            Event event = transientEvent(category, user, false);
            event.setState(PUBLISHED);
            event.setEventDate(EVENT_DATE.plusDays(i + 1));
            event.setPublishedOn(EVENT_PUBLISHED_ON);
            event.setParticipationLimit(participationLimit);
            saved.add(eventsRepository.save(event));
        }
        return saved;
    }

    public static List<EventShort> eventShortListFromEventList(List<Event> events) {
        return events.stream().map(TestData::eventShortFromEvent)
                .collect(Collectors.toList());
    }

    public static EventShort eventShortFromEvent(Event event) {
        return new EventShort(
                event.getId(),
                event.getAnnotation(),
                event.getCategory(),
                event.getEventDate(),
                event.getUser().getId(),
                event.getUser().getName(),
                event.getPaid(),
                event.getTitle(),
                event.getParticipationLimit(),
                event.getPublishedOn()
        );
    }

    public static List<EventShortDto> eventShortDtoListFromEventList(List<Event> events,
                                                                     Long confirmed,
                                                                     Long views) {
        return events.stream().map(e -> eventShortDtoFromEvent(e, confirmed, views))
                .collect(Collectors.toList());
    }

    public static EventShortDto eventShortDtoFromEvent(Event event, Long confirmed, Long views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryDtoFromCategory(event.getCategory()))
                .confirmedRequests(confirmed)
                .eventDate(event.getEventDate())
                .initiator(userShortDtoFromUser(event.getUser()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static UserShortDto userShortDtoFromUser(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static CategoryDto categoryDtoFromCategory(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static List<Category> getTenSavedCategories(CategoriesRepository categoriesRepository) {
        List<Category> tenTransientCategories = getTenTransientCategories();
        return tenTransientCategories.stream()
                .map(categoriesRepository::save)
                .collect(Collectors.toList());
    }

    public static List<EventLocation> getEqualNLocations(int n, EventLocation location) {
        return Collections.nCopies(n, location);
    }

    public static List<User> getTenSavedUsers(UsersRepository usersRepository) {
        List<User> tenTransientUsers = getTenTransientUsers();
        return tenTransientUsers.stream()
                .map(usersRepository::save)
                .collect(Collectors.toList());
    }

    public static Category saveCategory(Category category, CategoriesRepository categoriesRepository) {
        return categoriesRepository.save(category);
    }

    public static User saveUser(User user, UsersRepository usersRepository) {
        return usersRepository.save(user);
    }

    public static List<Request> save100RequestsForEvents(List<Event> events,
                                                         RequestsRepository requestsRepository,
                                                         UsersRepository usersRepository) {
        List<Request> requests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int eventIdx = i % events.size();
            Event event = events.get(eventIdx);
            User requester = saveUser(transientUser(USER_EMAIL + i + 100, USER_NAME + i + 100), usersRepository);
            Request request = transientRequest(event, requester, ParticipationStatus.CONFIRMED);
            requests.add(requestsRepository.save(request));
        }
        return requests;
    }

    public static Event getOneSavedEvent(EventsRepository eventsRepository,
                                         UsersRepository usersRepository,
                                         CategoriesRepository categoriesRepository) {
        User user = saveUser(transientUser(USER_EMAIL, USER_NAME), usersRepository);
        Category category = saveCategory(transientCategory(CATEGORY_NAME), categoriesRepository);
        Event event = transientEvent(category, user, false);
        event.setPublishedOn(EVENT_PUBLISHED_ON);
        return eventsRepository.save(event);
    }

    public static Request mockRequest(long id, ParticipationStatus status, int eventParticipationLimit) {
        return Request.builder()
                .id(id)
                .created(LocalDateTime.now())
                .event(mockEvent(10, eventParticipationLimit, false))
                .requester(mockEmptyUser())
                .status(status)
                .build();
    }

    public static Event mockEvent(long id, int participationLimit, boolean requestModeration) {
        return Event.builder()
                .id(id)
                .requestModeration(requestModeration)
                .participationLimit(participationLimit)
                .build();
    }

    public static User mockEmptyUser() {
        return User.builder().build();
    }
}
