package ru.practicum.ewm.service.stats.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.service.stats.common.util.DateUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class StatRequest {
    @NotEmpty
    private String app;
    @NotEmpty
    private String uri;
    @NotEmpty
    private String ip;
    @NotNull
    @JsonFormat(pattern = DateUtil.DATE_FORMAT)
    private LocalDateTime timestamp;
}
