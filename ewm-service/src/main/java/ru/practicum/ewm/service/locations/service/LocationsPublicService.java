package ru.practicum.ewm.service.locations.service;

import ru.practicum.ewm.service.locations.dto.LocationResponse;

import java.util.List;

public interface LocationsPublicService {

    List<LocationResponse> getLocations(int from, int size);

}
