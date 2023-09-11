package ru.practicum.ewm.service.categories.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.service.BaseIntegTest;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.error.EwmServiceException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.ewm.service.error.ErrorConstants.NOT_FOUND_REASON;
import static ru.practicum.ewm.service.testutil.TestConstants.CATEGORY_NAME;
import static ru.practicum.ewm.service.testutil.TestData.transientCategory;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CategoriesPublicServiceImplTest extends BaseIntegTest {

    private final CategoriesPublicServiceImpl publicService;
    private final CategoriesRepository repository;

    @Test
    void getCategories_whenNoCategories_returnsEmptyList() {
        List<CategoryDto> categories = publicService.getCategories(0, 10);
        assertThat(categories).isEmpty();
    }

    @Test
    void getCategories_whenAllOk_returnsCorrectListWithCorrectOffset() {
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            categories.add(saveCategory(CATEGORY_NAME + i));
        }
        List<CategoryDto> result = publicService.getCategories(3, 5)
                .stream()
                .sorted(Comparator.comparing(CategoryDto::getName))
                .collect(Collectors.toList());

        assertThat(result).hasSize(5);
        assertThat(result.get(0).getName()).isEqualTo(categories.get(3).getName());
        assertThat(result.get(1).getName()).isEqualTo(categories.get(4).getName());
        assertThat(result.get(2).getName()).isEqualTo(categories.get(5).getName());
        assertThat(result.get(3).getName()).isEqualTo(categories.get(6).getName());
        assertThat(result.get(4).getName()).isEqualTo(categories.get(7).getName());
    }

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