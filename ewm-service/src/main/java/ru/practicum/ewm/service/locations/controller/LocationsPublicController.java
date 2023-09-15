package ru.practicum.ewm.service.locations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.locations.dto.LocationResponse;
import ru.practicum.ewm.service.locations.service.LocationsPublicService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LocationsPublicController {

    private final LocationsPublicService publicService;

    @GetMapping("/locations")
    public List<LocationResponse> getLocations(@Valid @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                               @Valid @Min(1) @RequestParam(value = "size", defaultValue = "10") int size) {
        log.info("Received request to GET all locations starting from: {}, size: {}", from, size);
        List<LocationResponse> locations = publicService.getLocations(from, size);
        log.info("Retrieved locations: {}", locations);
        return locations;
    }
}
