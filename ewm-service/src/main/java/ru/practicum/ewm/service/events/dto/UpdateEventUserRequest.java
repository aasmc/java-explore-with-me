package ru.practicum.ewm.service.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateEventUserRequest implements UpdateEventRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_FORMAT)
    private LocalDateTime eventDate;
    private EventLocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private UserEventStateAction stateAction;
    @Size(min = 3, max = 120)
    private String title;
}
