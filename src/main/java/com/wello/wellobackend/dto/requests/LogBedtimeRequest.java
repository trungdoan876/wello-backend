package com.wello.wellobackend.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request DTO for logging bedtime (Quick Log Step 1)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogBedtimeRequest {
    private int userId;
    private LocalDateTime bedtime;
}
