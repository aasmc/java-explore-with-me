package ru.practicum.ewm.service.locations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.locations.domain.Location;
import ru.practicum.ewm.service.locations.dto.LocationResponse;
import ru.practicum.ewm.service.locations.mapper.LocationsMapper;
import ru.practicum.ewm.service.locations.repository.LocationsRepository;
import ru.practicum.ewm.service.util.OffsetBasedPageRequest;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationsPublicServiceImpl implements LocationsPublicService {

    private final LocationsRepository locationsRepository;
    private final LocationsMapper mapper;

    @Override
    public List<LocationResponse> getLocations(int from, int size) {
        Pageable pageable = new OffsetBasedPageRequest(from, size);
        List<Location> locations = locationsRepository.findAll(pageable).getContent();
        return mapper.mapToDtoList(locations);
    }
}
