package ru.practicum.ewm.service.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.dto.NewCategoryDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoriesAdminController {

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto dto) {
        log.info("Received admin POST request to create new category: {}", dto);
        // TODO Обратите внимание: имя категории должно быть уникальным
        return null;
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("catId") Long catId) {
        log.info("Received admin request to DELETE category with ID={}", catId);
        // TODO For a category to be deleted it cannot have any events.
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable("catId") Long catId,
                                      @RequestBody NewCategoryDto dto) {
        log.info("Received admin PATCH request to update category with ID={}. To be updated to: {}",
                catId, dto);
        // TODO Обратите внимание: имя категории должно быть уникальным
        return null;
    }

}
