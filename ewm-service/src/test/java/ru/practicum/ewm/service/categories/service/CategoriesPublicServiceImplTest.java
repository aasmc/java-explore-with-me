package ru.practicum.ewm.service.categories.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.service.BaseIntegTest;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.error.EwmServiceException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.ewm.service.error.ErrorConstants.*;
import static ru.practicum.ewm.service.util.TestConstants.*;
import static ru.practicum.ewm.service.util.TestData.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoriesPublicServiceImplTest extends BaseIntegTest {

    private final CategoriesPublicServiceImpl publicService;
    private final CategoriesRepository repository;

    @Test
    void getCategory_whenAllOk_returnsCategory() {
        Category category = saveCategory(CATEGORY_NAME);
        CategoryDto dto = publicService.getCategory(category.getId());
        assertThat(dto.getId()).isEqualTo(category.getId());
        assertThat(dto.getName()).isEqualTo(category.getName());
    }

    @Test
    void getCategory_whenNotFound_throws() {
        EwmServiceException ex = assertThrows(EwmServiceException.class,
                () -> publicService.getCategory(100L));
        assertThat(ex.getReason()).isEqualTo(NOT_FOUND_REASON);
    }

    private Category saveCategory(String name) {
        Category category = transientCategory(name);
        return repository.saveAndFlush(category);
    }

}