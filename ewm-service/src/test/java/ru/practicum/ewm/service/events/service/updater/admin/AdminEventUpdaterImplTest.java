package ru.practicum.ewm.service.events.service.updater.admin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.AdminEventStateAction;
import ru.practicum.ewm.service.events.dto.EventState;
import ru.practicum.ewm.service.events.dto.UpdateEventAdminRequest;
import ru.practicum.ewm.service.usermanagement.domain.User;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.ewm.service.error.ErrorConstants.NOT_FOUND_REASON;
import static ru.practicum.ewm.service.testutil.TestConstants.USER_EMAIL;
import static ru.practicum.ewm.service.testutil.TestConstants.USER_NAME;
import static ru.practicum.ewm.service.testutil.TestData.*;

@ExtendWith(MockitoExtension.class)
class AdminEventUpdaterImplTest {

    @Mock
    private CategoriesRepository categoriesRepository;
    @InjectMocks
    private AdminEventUpdaterImpl updater;

    @Test
    void updateEvent_updatesEvent() {
        UpdateEventAdminRequest request = updateEventAdminRequestPublishAction();
        Category newCat = Category.builder().id(request.getCategory()).name("New Cat").build();
        User user = transientUser(USER_EMAIL, USER_NAME);
        user.setId(1L);
        Mockito
                .when(categoriesRepository.findById(request.getCategory()))
                .thenReturn(Optional.of(newCat));
        Event event = transientEvent(2L, 2L, false, "Cat", "User", "user@email.com");
        event.setId(1L);
        Event result = updater.updateEvent(event, request);
        Event expected = eventFromUpdateRequest(request, event.getId(), user, newCat);
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getAnnotation()).isEqualTo(expected.getAnnotation());
        assertThat(result.getCategory()).isEqualTo(expected.getCategory());
        assertThat(result.getDescription()).isEqualTo(expected.getDescription());
        assertThat(result.getEventDate()).isEqualTo(expected.getEventDate());
        assertThat(result.getLocation()).isEqualTo(expected.getLocation());
        assertThat(result.getPaid()).isEqualTo(expected.getPaid());
        assertThat(result.getParticipationLimit()).isEqualTo(expected.getParticipationLimit());
        assertThat(result.getRequestModeration()).isEqualTo(expected.getRequestModeration());
        assertThat(result.getTitle()).isEqualTo(expected.getTitle());

        if (request.getStateAction() == AdminEventStateAction.REJECT_EVENT) {
            assertThat(result.getState()).isEqualTo(EventState.CANCELED);
        } else if (request.getStateAction() == AdminEventStateAction.PUBLISH_EVENT) {
            assertThat(result.getState()).isEqualTo(EventState.PUBLISHED);
        }
    }

    @Test
    void updateEvent_throws_whenUpdatedCategoryNotFound() {
        UpdateEventAdminRequest request = updateEventAdminRequestPublishAction();

        Mockito
                .when(categoriesRepository.findById(request.getCategory()))
                .thenReturn(Optional.empty());

        Event event = transientEvent(2L, 2L, false, "Cat", "User", "user@email.com");
        EwmServiceException ex = assertThrows(EwmServiceException.class,
                () -> updater.updateEvent(event, request));
        assertThat(ex.getReason()).isEqualTo(NOT_FOUND_REASON);
    }

}