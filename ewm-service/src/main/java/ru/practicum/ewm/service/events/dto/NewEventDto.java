package ru.practicum.ewm.service.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_FORMAT)
    private LocalDateTime eventDate;
    @NotNull
    private EventLocationDto location;
    private boolean paid;
    /**
     * Value 0 means there's no limit.
     */
    private int participantLimit;
    /**
     * If true, all requests to participate in the event will be waiting to be
     * confirmed by initiator of the event. Otherwise will be confirmed automatically.
     */
    private boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120)
    private String title;
}
