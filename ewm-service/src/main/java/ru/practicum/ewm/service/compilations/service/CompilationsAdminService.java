package ru.practicum.ewm.service.compilations.service;

import ru.practicum.ewm.service.compilations.dto.CompilationDto;
import ru.practicum.ewm.service.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.service.compilations.dto.UpdateCompilationRequest;

public interface CompilationsAdminService {

    CompilationDto createCompilation(NewCompilationDto dto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest dto);

}
