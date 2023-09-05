package ru.practicum.ewm.service.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.compilations.dto.CompilationDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CompilationsPublicController {

    @GetMapping("/compilations")
    public List<CompilationDto> getCompilations(@RequestParam(value = "pinned", required = false) Boolean pinned,
                                                @Valid @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                                @Valid @Min(1) @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Received request public to GET compilations. From: {}, size: {}", from, size);
        // TODO
        return Collections.emptyList();
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable("compId") Long compId) {
        log.info("Received public request to GET compilation with ID={}", compId);
        // TODO
        return null;
    }

}
