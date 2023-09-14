package ru.practicum.ewm.service.locations.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.ewm.service.BaseIntegTest;
import ru.practicum.ewm.service.locations.domain.Coordinates;
import ru.practicum.ewm.service.locations.domain.Location;
import ru.practicum.ewm.service.locations.dto.CreateLocationRequest;
import ru.practicum.ewm.service.locations.dto.LocationResponse;
import ru.practicum.ewm.service.locations.repository.LocationsRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.ewm.service.testutil.TestConstants.*;
import static ru.practicum.ewm.service.testutil.TestData.createLuzhnikiLoc;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LocationsAdminServiceImplTest extends BaseIntegTest {

    private final LocationsAdminService locationsAdminService;
    private final LocationsRepository locationsRepository;

    @Test
    void createLocation_whenLatLonInDb_throws() {
        Location location = Location.builder().coordinates(Coordinates.builder().radius(RADIUS).lat(LUZHNIKI_LAT).lon(LUZHNIKI_LON).build()).name("Luzhniki").build();
        locationsRepository.save(location);
        CreateLocationRequest luzhnikiLoc = createLuzhnikiLoc();
        assertThrows(DataIntegrityViolationException.class,
                () -> locationsAdminService.createLocation(luzhnikiLoc));
    }

    @Test
    void createLocation_whenAllOk_createsLocation() {
        CreateLocationRequest luzhnikiLoc = createLuzhnikiLoc();
        LocationResponse response = locationsAdminService.createLocation(luzhnikiLoc);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getLat()).isEqualTo(LUZHNIKI_LAT);
        assertThat(response.getLon()).isEqualTo(LUZHNIKI_LON);
    }

}