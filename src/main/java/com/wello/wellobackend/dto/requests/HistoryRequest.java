package com.wello.wellobackend.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoryRequest {
    private double weight;
    private int height;
    private String goal;
    private String activityLevel;
}
