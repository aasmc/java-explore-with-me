package ru.practicum.ewm.service.locations.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.locations.domain.Coordinates;
import ru.practicum.ewm.service.locations.domain.Location;
import ru.practicum.ewm.service.locations.dto.CreateLocationRequest;
import ru.practicum.ewm.service.locations.dto.LocationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationsMapper {

    public Location mapToEntity(CreateLocationRequest dto) {
        return Location.builder()
                .coordinates(Coordinates.builder()
                        .lat(dto.getLat())
                        .lon(dto.getLon())
                        .radius(dto.getRadius()).build())
                .name(dto.getName())
                .build();
    }

    public LocationResponse mapToDto(Location entity) {
        return LocationResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lat(entity.getCoordinates().getLat())
                .lon(entity.getCoordinates().getLon())
                .build();
    }

    public List<LocationResponse> mapToDtoList(List<Location> locations) {
        return locations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
