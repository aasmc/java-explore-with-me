package ru.practicum.ewm.service.categories.service;

import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.dto.NewCategoryDto;

public interface CategoriesAdminService {

    CategoryDto createCategory(NewCategoryDto dto);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(NewCategoryDto dto, Long catId);
}
