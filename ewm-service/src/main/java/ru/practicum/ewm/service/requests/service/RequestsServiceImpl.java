package ru.practicum.ewm.service.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.service.error.EwmServiceException;
import ru.practicum.ewm.service.events.domain.Event;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.requests.dto.ParticipationStatus;
import ru.practicum.ewm.service.requests.mapper.RequestsMapper;
import ru.practicum.ewm.service.requests.repository.RequestsRepository;
import ru.practicum.ewm.service.usermanagement.domain.User;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestsServiceImpl implements RequestsService {

    private final RequestsRepository requestsRepository;
    private final RequestsMapper mapper;
    private final RequestValidator validator;

    @Override
    public List<ParticipationRequestDto> getRequestDtosForEventOfUser(Long userId, Long eventId) {
        List<Request> requests = requestsRepository.findAllByEvent_User_IdAndEvent_Id(userId, eventId);
        return mapper.mapToDtoList(requests);
    }

    @Override
    public List<Request> getRequestsForEventOfUser(Long userId, Long eventId, List<Long> requestIds) {
        return requestsRepository.findAllByIdInAndEvent_User_IdAndEvent_Id(requestIds, userId, eventId);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsOfUser(Long userId) {
        validator.validateUserExists(userId);
        List<Request> requests = requestsRepository.findAllByRequester_Id(userId);
        return mapper.mapToDtoList(requests);
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User user = validator.getUserOrThrow(userId);
        Event event = validator.getEventOrThrow(eventId);
        validator.validateUserNotInitiator(userId, event);
        validator.validateEventPublished(event);
        validator.validateParticipationLimitNotReached(event);
        Request request = transientRequest(user, event);
        try {
            request = requestsRepository.save(request);
            return mapper.mapToDto(request);
        } catch (DataIntegrityViolationException ex) {
            throw EwmServiceException.dataIntegrityException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        validator.validateUserExists(userId);
        Request request = validator.getRequestOrThrow(requestId);
        request.setStatus(ParticipationStatus.CANCELED);
        request = requestsRepository.save(request);
        return mapper.mapToDto(request);
    }

    private Request transientRequest(User user, Event event) {
        ParticipationStatus status;
        if (!event.getRequestModeration() || event.getParticipationLimit() == 0) {
            status = ParticipationStatus.CONFIRMED;
        } else {
            status = ParticipationStatus.PENDING;
        }
        return Request.builder()
                .event(event)
                .requester(user)
                .status(status)
                .build();

    }
}
