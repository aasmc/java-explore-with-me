package ru.practicum.ewm.service.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.categories.dto.CategoryDto;
import ru.practicum.ewm.service.usermanagement.dto.UserShortDto;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import java.time.LocalDateTime;

@Data
@Builder
public class EventShortDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_FORMAT)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
}
