package com.wello.wellobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyStreakResponse {
    private LocalDate date;
    private boolean water;
    private boolean meal;
}
