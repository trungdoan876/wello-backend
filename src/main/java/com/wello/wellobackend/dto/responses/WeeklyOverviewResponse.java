package com.wello.wellobackend.dto.responses;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyOverviewResponse {
    private List<DayOverview> weekData;
    private LocalDate currentDate;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DayOverview {
        private LocalDate date;
        private String dayOfWeek;
        private int caloriesConsumed;
        private boolean hasData;
    }
}
