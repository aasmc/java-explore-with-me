package ru.practicum.ewm.service.compilations.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class NewCompilationDto {
    @Builder.Default
    private Set<Long> events = new HashSet<>();
    private boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
