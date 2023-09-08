package ru.practicum.ewm.service.categories.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.service.BaseIntegTest;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.dto.NewCategoryDto;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.repository.EventsRepository;
import ru.practicum.ewm.service.usermanagement.domain.User;
import ru.practicum.ewm.service.usermanagement.repository.UsersRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.ewm.service.error.ErrorConstants.DATA_INTEGRITY_VIOLATION_REASON;
import static ru.practicum.ewm.service.error.ErrorConstants.NOT_FOUND_REASON;
import static ru.practicum.ewm.service.util.TestConstants.*;
import static ru.practicum.ewm.service.util.TestData.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoriesAdminServiceImplTest extends BaseIntegTest {

    private final CategoriesAdminServiceImpl service;
    private final CategoriesRepository categoriesRepository;
    private final EventsRepository eventsRepository;
    private final UsersRepository usersRepository;

    @Test
    void updateCategory_notFound_throws() {
        NewCategoryDto toUpdate = NewCategoryDto.builder().name(CATEGORY_NAME).build();

        EwmServiceException ex = assertThrows(EwmServiceException.class, () ->
                service.updateCategory(toUpdate, 100L));
        assertThat(ex.getReason()).isEqualTo(NOT_FOUND_REASON);
    }

    @Test
    void updateCategory_notUniqueName_throws() {
        Category category = saveCategory(CATEGORY_NAME);
        saveCategory(CATEGORY_NAME + 1);

        NewCategoryDto toUpdate = NewCategoryDto.builder().name(CATEGORY_NAME + 1).build();

        EwmServiceException ex = assertThrows(EwmServiceException.class, () ->
                service.updateCategory(toUpdate, category.getId()));
        assertThat(ex.getReason()).isEqualTo(DATA_INTEGRITY_VIOLATION_REASON);
    }

    @Test
    void updateCategory_sameName_nothingHappens() {
        Category category = saveCategory(CATEGORY_NAME);

        NewCategoryDto toUpdate = NewCategoryDto.builder().name(CATEGORY_NAME).build();
        CategoryDto updated = service.updateCategory(toUpdate, category.getId());
        assertThat(updated.getId()).isEqualTo(category.getId());
        assertThat(updated.getName()).isEqualTo(toUpdate.getName());
    }

    @Test
    void updateCategory_whenAllOk_updatesCategory() {
        Category category = saveCategory(CATEGORY_NAME);

        NewCategoryDto toUpdate = NewCategoryDto.builder().name("New Name").build();
        CategoryDto updated = service.updateCategory(toUpdate, category.getId());
        assertThat(updated.getId()).isEqualTo(category.getId());
        assertThat(updated.getName()).isEqualTo(toUpdate.getName());
    }

    @Test
    void deleteCategory_whenCategoryHasEvents_throws() {
        Category category = saveCategory(CATEGORY_NAME);
        User user = saveUser();

        Event transientEvent = transientEvent(category.getId(),
                user.getId(),
                false,
                category.getName(),
                user.getName(),
                user.getEmail());
        eventsRepository.saveAndFlush(transientEvent);

        EwmServiceException ex = assertThrows(EwmServiceException.class, () ->
                service.deleteCategory(category.getId()));
        assertThat(ex.getReason()).isEqualTo(DATA_INTEGRITY_VIOLATION_REASON);

    }

    @Test
    void deleteCategory_whenNotFound_throws() {
        EwmServiceException ex = assertThrows(EwmServiceException.class, () ->
                service.deleteCategory(100L));
        assertThat(ex.getReason()).isEqualTo(NOT_FOUND_REASON);
    }

    @Test
    void deleteCategory_whenAllOk_deletesCategory() {
        Category category = saveCategory(CATEGORY_NAME);
        service.deleteCategory(category.getId());

        Optional<Category> byId = categoriesRepository.findById(category.getId());
        assertThat(byId).isEmpty();
    }

    @Test
    void createCategory_throws_whenNotUniqueName() {
        saveCategory(CATEGORY_NAME);

        NewCategoryDto dto = newCategoryDto(CATEGORY_NAME);
        EwmServiceException ex = assertThrows(EwmServiceException.class, () -> {
            service.createCategory(dto);
        });
        assertThat(ex.getReason()).isEqualTo(DATA_INTEGRITY_VIOLATION_REASON);
    }

    @Test
    void createCategory_whenAllCorrect_createsNewCategory() {
        NewCategoryDto dto = newCategoryDto(CATEGORY_NAME);
        CategoryDto expected = fromNewCategoryDto(dto);
        CategoryDto result = service.createCategory(dto);
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(expected.getName());
    }

    private Category saveCategory(String name) {
        Category category = transientCategory(name);
        return categoriesRepository.saveAndFlush(category);
    }

    private User saveUser() {
        User user = transientUser(USER_NAME, USER_EMAIL);
        return usersRepository.saveAndFlush(user);
    }

}