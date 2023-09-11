package ru.practicum.ewm.service.categories.service;

import ru.practicum.ewm.service.categories.dto.CategoryDto;

import java.util.List;

public interface CategoriesPublicService {

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(Long catId);

}
