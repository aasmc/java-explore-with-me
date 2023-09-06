package ru.practicum.ewm.service.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.categories.domain.Category;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.categories.dto.NewCategoryDto;
import ru.practicum.ewm.service.categories.mapper.CategoriesMapper;
import ru.practicum.ewm.service.categories.repository.CategoriesRepository;
import ru.practicum.ewm.service.error.EwmServiceException;

import java.util.Optional;

import static ru.practicum.ewm.service.error.ErrorConstants.CATEGORY_NOT_FOUND_MSG;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoriesAdminServiceImpl implements CategoriesAdminService {


    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper mapper;

    @Override
    public CategoryDto createCategory(NewCategoryDto dto) {
        try {
            Category raw = mapper.mapToDomain(dto);
            return mapper.mapToDto(categoriesRepository.saveAndFlush(raw));
        } catch (DataIntegrityViolationException ex) {
            throw EwmServiceException.dataIntegrityException(ex.getMessage());
        }
    }

    @Override
    public void deleteCategory(Long catId) {
        try {
            Optional<Category> byId = categoriesRepository.findById(catId);
            if (byId.isEmpty()) {
                String msg = String.format(CATEGORY_NOT_FOUND_MSG, catId);
                throw EwmServiceException.notFoundException(msg);
            }
            categoriesRepository.delete(byId.get());
            categoriesRepository.flush();
        } catch (DataIntegrityViolationException ex) {
            throw EwmServiceException.dataIntegrityException(ex.getMessage());
        }
    }

    @Override
    public CategoryDto updateCategory(NewCategoryDto dto, Long catId) {
        try {
            Category category = categoriesRepository.findById(catId).orElseThrow(() -> {
                String msg = String.format(CATEGORY_NOT_FOUND_MSG, catId);
                return EwmServiceException.notFoundException(msg);
            });
            category.setName(dto.getName());
            return mapper.mapToDto(categoriesRepository.saveAndFlush(category));
        } catch (DataIntegrityViolationException ex) {
            throw EwmServiceException.dataIntegrityException(ex.getMessage());
        }
    }
}
