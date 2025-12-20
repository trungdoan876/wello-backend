package com.wello.wellobackend.dto.responses;

import com.wello.wellobackend.enums.ActivityLevel;
import com.wello.wellobackend.enums.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryResponse {
    private int id;
    private int userId;
    private double weight;
    private int height;
    private Goal goal;
    private ActivityLevel activityLevel;
    private LocalDateTime recordedAt;
}
