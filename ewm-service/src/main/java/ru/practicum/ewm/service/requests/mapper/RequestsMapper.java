package ru.practicum.ewm.service.requests.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.service.requests.domain.Request;
import ru.practicum.ewm.service.requests.dto.ParticipationRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestsMapper {

    public ParticipationRequestDto mapToDto(Request request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .requester(request.getRequester().getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .build();
    }

    public List<ParticipationRequestDto> mapToDtoList(List<Request> requests) {
        return requests.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

}
