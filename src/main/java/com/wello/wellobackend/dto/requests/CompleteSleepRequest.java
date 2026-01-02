package com.wello.wellobackend.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Request DTO for completing sleep log (Quick Log Step 2)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompleteSleepRequest {
    private int userId;
    private LocalDateTime wakeTime;
    private Integer quality; // Optional (1-5)
}
