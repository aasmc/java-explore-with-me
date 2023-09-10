package ru.practicum.ewm.service.events.service.updater.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.events.dto.EventRequestStatusUpdateResult;
import ru.practicum.ewm.service.events.service.statisticsservice.StatisticsService;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.requests.mapper.RequestsMapper;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.practicum.ewm.service.error.ErrorConstants.CONDITIONS_NOT_MET_REASON;
import static ru.practicum.ewm.service.testutil.TestData.mockEvent;
import static ru.practicum.ewm.service.testutil.TestData.mockRequest;

@ExtendWith(MockitoExtension.class)
class ConfirmRequestUpdaterTest {

    @Mock
    private RequestsRepository requestsRepository;
    @Mock
    private RequestsMapper mapper;
    @Mock
    private StatisticsService statisticsService;
    @InjectMocks
    private ConfirmRequestUpdater updater;

    @Test
    void updateRequests_ifHasParticipationLimit_someRequestsConfirmed_someRejected() {
        List<Request> requests = List.of(
                mockRequest(1, ParticipationStatus.PENDING, 10),
                mockRequest(2, ParticipationStatus.PENDING, 10)
        );

        List<Request> confirmed = List.of(
                mockRequest(1, ParticipationStatus.CONFIRMED, 10)
        );
        List<Request> rejected = List.of(
                mockRequest(2, ParticipationStatus.REJECTED, 10)
        );

        List<Request> all = new ArrayList<>(confirmed);
        all.addAll(rejected);

        List<ParticipationRequestDto> confirmedDtos = List.of(
                ParticipationRequestDto.builder().id(1L).status(ParticipationStatus.CONFIRMED).build()
        );
        List<ParticipationRequestDto> rejectedDtos = List.of(
                ParticipationRequestDto.builder().id(2L).status(ParticipationStatus.REJECTED).build()
        );

        Event event = mockEvent(1,2, true);
        Mockito
                .when(statisticsService.getConfirmedCountForEvent(event.getId()))
                        .thenReturn(1L);
        Mockito
                .when(requestsRepository.saveAll(requests))
                .thenReturn(all);
        Mockito
                .when(mapper.mapToDtoList(confirmed))
                .thenReturn(confirmedDtos);
        Mockito
                .when(mapper.mapToDtoList(rejected))
                .thenReturn(rejectedDtos);
        EventRequestStatusUpdateResult result = updater.updateRequests(requests, event);
        assertThat(result.getConfirmedRequests()).hasSize(1);
        assertThat(result.getRejectedRequests()).hasSize(1);
    }

    @Test
    void updateRequests_ifEventHasNoParticipationRestrictions_confirmsAll() {
        List<Request> requests = List.of(
                mockRequest(1, ParticipationStatus.PENDING, 10),
                mockRequest(2, ParticipationStatus.PENDING, 10)
        );

        List<Request> confirmed = List.of(
                mockRequest(1, ParticipationStatus.CONFIRMED, 10),
                mockRequest(2, ParticipationStatus.CONFIRMED, 10)
        );
        List<ParticipationRequestDto> confirmedDtos = List.of(
                ParticipationRequestDto.builder().id(1L).status(ParticipationStatus.CONFIRMED).build(),
                ParticipationRequestDto.builder().id(2L).status(ParticipationStatus.CONFIRMED).build()
        );

        Event event = mockEvent(1,0, false);
        Mockito
                .verifyNoInteractions(statisticsService);
        Mockito
                .when(requestsRepository.saveAll(requests))
                .thenReturn(confirmed);
        Mockito
                .when(mapper.mapToDtoList(confirmed))
                .thenReturn(confirmedDtos);

        EventRequestStatusUpdateResult result = updater.updateRequests(requests, event);
        assertThat(result.getConfirmedRequests()).hasSize(2);
        assertThat(result.getRejectedRequests()).isEmpty();
    }

    @Test
    void updateRequests_ifAnyRequestIsNotPending_throws() {
        List<Request> requests = List.of(
                mockRequest(1, ParticipationStatus.CONFIRMED, 10),
                mockRequest(2, ParticipationStatus.PENDING, 10)
        );
        Event event = mockEvent(1,10, false);
        EwmServiceException ex = assertThrows(EwmServiceException.class,
                () -> updater.updateRequests(requests, event));
        assertThat(ex.getReason()).isEqualTo(CONDITIONS_NOT_MET_REASON);
    }

}