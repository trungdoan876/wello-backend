package com.wello.wellobackend.dto.requests;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddWaterIntakeRequest {
    private int userId;
    private int amountMl;
    private LocalDate date;
}
