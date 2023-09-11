package ru.practicum.ewm.service.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.service.CategoriesPublicService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoriesPublicController {

    private final CategoriesPublicService publicService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@Valid @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                           @Valid @Min(1) @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Received public request to GET all categories, From: {}, size: {}", from, size);
        return publicService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable("catId") Long catId) {
        log.info("Received public request to GET category with ID={}", catId);
        return publicService.getCategory(catId);
    }

}
