package ru.practicum.ewm.service.events.service.updater.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.requests.mapper.RequestsMapper;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.practicum.ewm.service.testutil.TestData.mockEvent;
import static ru.practicum.ewm.service.testutil.TestData.mockRequest;

@ExtendWith(MockitoExtension.class)
class RejectRequestUpdaterTest {

    @Mock
    private RequestsRepository requestsRepository;
    @Mock
    private RequestsMapper mapper;
    @InjectMocks
    private RejectRequestUpdater updater;

    @Test
    void updateRequests_rejectsAllRequests() {
        List<Request> requests = List.of(
                mockRequest(1, ParticipationStatus.PENDING, 10),
                mockRequest(2, ParticipationStatus.PENDING, 10)
        );

        List<Request> rejected = List.of(
                mockRequest(1, ParticipationStatus.CONFIRMED, 10),
                mockRequest(2, ParticipationStatus.REJECTED, 10)
        );

        List<ParticipationRequestDto> rejectedDtos = List.of(
                ParticipationRequestDto.builder().id(1L).status(ParticipationStatus.REJECTED).build(),
                ParticipationRequestDto.builder().id(2L).status(ParticipationStatus.REJECTED).build()
        );

        Event event = mockEvent(1,2, true);

        Mockito
                .when(requestsRepository.saveAll(requests))
                .thenReturn(rejected);
        Mockito
                .when(mapper.mapToDtoList(rejected))
                .thenReturn(rejectedDtos);
        EventRequestStatusUpdateResult result = updater.updateRequests(requests, event);
        assertThat(result.getConfirmedRequests()).isEmpty();
        assertThat(result.getRejectedRequests()).hasSize(2);
    }

}