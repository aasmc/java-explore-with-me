package ru.practicum.ewm.service.locations.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.service.BaseIntegTest;
import ru.practicum.ewm.service.locations.dto.LocationResponse;
import ru.practicum.ewm.service.locations.repository.LocationsRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.ewm.service.testutil.TestData.createTenSavedLocations;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
class LocationsPublicServiceImplTest extends BaseIntegTest {

    private final LocationsPublicService locationsPublicService;
    private final LocationsRepository locationsRepository;

    @Test
    void getLocations_whenHasLocations_returnsCorrectList() {
        createTenSavedLocations(locationsRepository);
        List<LocationResponse> response = locationsPublicService.getLocations(0, 5);
        assertThat(response).hasSize(5);
    }

    @Test
    void getLocations_whenNoLocations_returnsEmptyList() {
        List<LocationResponse> locations = locationsPublicService.getLocations(0, 10);
        assertThat(locations).isEmpty();
    }

}