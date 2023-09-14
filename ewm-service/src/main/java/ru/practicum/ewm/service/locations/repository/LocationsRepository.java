package ru.practicum.ewm.service.locations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.service.locations.domain.Location;

import java.util.Optional;

public interface LocationsRepository extends JpaRepository<Location, Long> {

    Optional<Location> findByCoordinates_LatAndCoordinatesLon(float lat, float lon);

}
