package ru.practicum.ewm.service.stats.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatResponse {
    private String app;
    private String uri;
    private Long hits;
}
