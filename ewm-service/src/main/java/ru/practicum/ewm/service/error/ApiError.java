package ru.practicum.ewm.service.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import java.time.LocalDateTime;

@Data
@Builder
public class ApiError {
    private String message;
    private String reason;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.DATE_FORMAT)
    private LocalDateTime timestamp;
}
