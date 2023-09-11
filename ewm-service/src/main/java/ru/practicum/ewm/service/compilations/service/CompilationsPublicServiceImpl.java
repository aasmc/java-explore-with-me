package ru.practicum.ewm.service.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.compilations.domain.Compilation;
import ru.practicum.ewm.service.compilations.dto.CompilationDto;
import ru.practicum.ewm.service.compilations.repository.CompilationsRepository;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.util.OffsetBasedPageRequest;

import java.util.List;

import static ru.practicum.ewm.service.error.ErrorConstants.COMPILATION_NOT_FOUND_MSG;

@Service
@Transactional
@RequiredArgsConstructor
public class CompilationsPublicServiceImpl implements CompilationsPublicService {

    private final CommonCompilationService commonService;
    private final CompilationsRepository compilationsRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        List<Compilation> compilations = null == pinned ?
                compilationsRepository.findAll(pageable).getContent() :
                compilationsRepository.findAllByPinned(pinned, pageable);
        return commonService.toCompilationDtoList(compilations);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> {
                    String msg = String.format(COMPILATION_NOT_FOUND_MSG, compId);
                    return EwmServiceException.notFoundException(msg);
                });
        return commonService.toCompilationDto(compilation);
    }
}
