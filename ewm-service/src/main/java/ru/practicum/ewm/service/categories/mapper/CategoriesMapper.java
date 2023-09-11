package ru.practicum.ewm.service.categories.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.dto.NewCategoryDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoriesMapper {

    public List<CategoryDto> mapToDtoList(List<Category> categories) {
        return categories.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

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
