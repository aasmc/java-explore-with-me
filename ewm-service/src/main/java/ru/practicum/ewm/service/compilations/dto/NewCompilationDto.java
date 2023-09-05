package ru.practicum.ewm.service.compilations.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class NewCompilationDto {
    private Set<Long> events;
    private boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
}
