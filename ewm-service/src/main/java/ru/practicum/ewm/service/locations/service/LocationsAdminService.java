package ru.practicum.ewm.service.locations.service;

import ru.practicum.ewm.service.locations.dto.CreateLocationRequest;
import ru.practicum.ewm.service.locations.dto.LocationResponse;

public interface LocationsAdminService {

    LocationResponse createLocation(CreateLocationRequest dto);

}
