package ru.practicum.ewm.service.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.mapper.CategoriesMapper;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.util.OffsetBasedPageRequest;

import java.util.List;

import static ru.practicum.ewm.service.error.ErrorConstants.CATEGORY_NOT_FOUND_MSG;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoriesPublicServiceImpl implements CategoriesPublicService {

    private final CategoriesRepository repository;
    private final CategoriesMapper mapper;

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        Page<Category> all = repository.findAll(pageable);
        List<Category> categories = all.getContent();
        return mapper.mapToDtoList(categories);
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = repository.findById(catId)
                .orElseThrow(() -> {
                    String msg = String.format(CATEGORY_NOT_FOUND_MSG, catId);
                    return EwmServiceException.notFoundException(msg);
                });
        return mapper.mapToDto(category);
    }
}
