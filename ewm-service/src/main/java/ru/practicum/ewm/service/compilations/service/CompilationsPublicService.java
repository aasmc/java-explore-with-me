package ru.practicum.ewm.service.compilations.service;

import ru.practicum.ewm.service.compilations.dto.CompilationDto;

import java.util.List;

public interface CompilationsPublicService {

    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    CompilationDto getCompilation(Long compId);

}
