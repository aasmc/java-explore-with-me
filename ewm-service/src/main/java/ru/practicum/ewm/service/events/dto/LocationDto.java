package ru.practicum.ewm.service.events.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto {
    private float lat;
    private float lon;
}
