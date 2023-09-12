package ru.practicum.ewm.service.locations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.service.locations.dto.CreateLocationRequest;
import ru.practicum.ewm.service.locations.dto.LocationResponse;
import ru.practicum.ewm.service.locations.service.LocationsAdminService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LocationsAdminController {

    private final LocationsAdminService adminService;

    // 1. Создание локации (название, ширина, долгота, радиус)
    // 2. В одной коордитане может быть только одна локация (уникальность на lat, lon)

    @PostMapping("/admin/locations")
    public LocationResponse createLocation(@RequestBody @Valid CreateLocationRequest dto) {
        log.info("Received POST request to create location: {}", dto);
        LocationResponse location = adminService.createLocation(dto);
        log.info("Created location: {}", location);
        return location;
    }

}
