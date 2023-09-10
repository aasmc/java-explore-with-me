package ru.practicum.ewm.service.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.service.compilations.dto.CompilationDto;
import ru.practicum.ewm.service.compilations.dto.NewCompilationDto;
import ru.practicum.ewm.service.compilations.dto.UpdateCompilationRequest;
import ru.practicum.ewm.service.compilations.service.CompilationsAdminService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CompilationsAdminController {

    private final CompilationsAdminService adminService;

    @PostMapping("/admin/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@RequestBody @Valid NewCompilationDto dto) {
        log.info("Received admin POST request to create new compilation: {}", dto);
        return adminService.createCompilation(dto);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable("compId") Long compId) {
        log.info("Received admin request to DELETE compilation with ID={}", compId);
        adminService.deleteCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable("compId") Long compId,
                                            @RequestBody @Valid UpdateCompilationRequest dto) {
        log.info("Received admin request to PATCH compilation with ID={}. To be updated to: {}",
                compId, dto);
        return adminService.updateCompilation(compId, dto);
    }
}
