package com.wello.wellobackend.dto.requests;

import com.wello.wellobackend.enums.ActivityLevel;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateActivityLevelRequest {
    private ActivityLevel activityLevel;
}
