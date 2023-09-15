package ru.practicum.ewm.service.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateLocationRequest {

    @Positive
    @NotNull
    private Float radius;
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
    @Size(min = 5, max = 50)
    @NotBlank
    private String name;

}
