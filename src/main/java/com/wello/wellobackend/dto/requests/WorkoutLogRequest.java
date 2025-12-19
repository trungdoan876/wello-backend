package com.wello.wellobackend.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogRequest {
    private int userId;
    private int exerciseId;
    private int durationMinutes;
    private LocalDate date;
}
