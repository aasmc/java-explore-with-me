package ru.practicum.ewm.service.locations.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.locations.domain.Location;
import ru.practicum.ewm.service.locations.dto.CreateLocationRequest;
import ru.practicum.ewm.service.locations.dto.LocationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationsMapper {

    public Location mapToEntity(CreateLocationRequest dto) {
        return Location.builder()
                .radius(dto.getRadius())
                .name(dto.getName())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }

    public LocationResponse mapToDto(Location entity) {
        return LocationResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lat(entity.getLat())
                .lon(entity.getLon())
                .build();
    }

    public List<LocationResponse> mapToDtoList(List<Location> locations) {
        return locations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
