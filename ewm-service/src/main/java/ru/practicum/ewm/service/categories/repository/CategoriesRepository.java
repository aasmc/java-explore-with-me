package ru.practicum.ewm.service.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.categories.domain.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}
