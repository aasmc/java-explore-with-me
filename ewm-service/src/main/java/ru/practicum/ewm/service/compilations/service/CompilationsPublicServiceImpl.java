package ru.practicum.ewm.service.compilations.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.compilations.domain.Compilation;
import ru.practicum.ewm.service.compilations.dto.CompilationDto;
import ru.practicum.ewm.service.compilations.mapper.CompilationsMapper;
import ru.practicum.ewm.service.compilations.repository.CompilationsRepository;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.util.DateHelper;
import ru.practicum.ewm.service.util.OffsetBasedPageRequest;

import java.util.List;

import static ru.practicum.ewm.service.error.ErrorConstants.COMPILATION_NOT_FOUND_MSG;

@Service
@Transactional
public class CompilationsPublicServiceImpl extends BaseCompilationService implements CompilationsPublicService {


    protected CompilationsPublicServiceImpl(StatisticsService statisticsService,
                                            CompilationsMapper mapper,
                                            CompilationsRepository compilationsRepository,
                                            DateHelper dateHelper) {
        super(statisticsService, mapper, compilationsRepository, dateHelper);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        List<Compilation> compilations;
        if (null == pinned) {
            compilations = compilationsRepository.findAll(pageable).getContent();
        } else {
            compilations = compilationsRepository.findAllByPinned(pinned, pageable);
        }
        return toCompilationDtoList(compilations);
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationsRepository.findById(compId)
                .orElseThrow(() -> {
                    String msg = String.format(COMPILATION_NOT_FOUND_MSG, compId);
                    return EwmServiceException.notFoundException(msg);
                });
        return toCompilationDto(compilation);
    }
}
