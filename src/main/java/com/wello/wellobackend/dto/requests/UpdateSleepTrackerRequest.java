package com.wello.wellobackend.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSleepTrackerRequest {
    private int userId;
    private LocalDateTime sleepTime;
    private LocalDateTime wakeTime;
    private Integer quality;
    private String notes;
}
