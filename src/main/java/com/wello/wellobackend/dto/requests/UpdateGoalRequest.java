package com.wello.wellobackend.dto.requests;

import com.wello.wellobackend.enums.Goal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGoalRequest {
    private Goal goal;
}
