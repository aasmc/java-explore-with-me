package ru.practicum.ewm.service.categories.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.dto.NewCategoryDto;

@Component
public class CategoriesMapper {

    public Category mapToDomain(NewCategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .build();
    }

    public CategoryDto mapToDto(Category entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

}
