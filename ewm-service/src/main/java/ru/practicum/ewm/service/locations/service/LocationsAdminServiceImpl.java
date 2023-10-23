package ru.practicum.ewm.service.locations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.locations.domain.Location;
import ru.practicum.ewm.service.locations.dto.CreateLocationRequest;
import ru.practicum.ewm.service.locations.dto.LocationResponse;
import ru.practicum.ewm.service.locations.mapper.LocationsMapper;
import ru.practicum.ewm.service.locations.repository.LocationsRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationsAdminServiceImpl implements LocationsAdminService {

    private final LocationsRepository locationsRepository;
    private final LocationsMapper mapper;

    @Override
    public LocationResponse createLocation(CreateLocationRequest dto) {
        Location location = mapper.mapToEntity(dto);
        location = locationsRepository.save(location);
        return mapper.mapToDto(location);
    }
}
