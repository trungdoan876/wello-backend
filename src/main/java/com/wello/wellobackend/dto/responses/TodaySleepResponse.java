package com.wello.wellobackend.dto.responses;

import com.wello.wellobackend.enums.SleepStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Response DTO for GET /api/sleep/today
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodaySleepResponse {
    private boolean hasRecord;
    private LocalDate date;

    // Only present if hasRecord=true
    private SleepStatus status;
    private SleepTrackerResponse data;
}
