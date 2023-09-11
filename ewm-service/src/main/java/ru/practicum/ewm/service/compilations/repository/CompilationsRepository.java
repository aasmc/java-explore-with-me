package ru.practicum.ewm.service.compilations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.compilations.domain.Compilation;

import java.util.List;

public interface CompilationsRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

}
