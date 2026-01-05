package com.wello.wellobackend.dto.responses;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// ket qua tinh toan muc tieu suc khoe
public class TargetResponse {
    private double bmi;
    private String bmiStatus; // Thêm cái này để báo: "Bình thường", "Thừa cân"...
    private double bmr;
    private double tdee;
    private int dailyCalories;

    // Phần Macros
    private int proteinGram;
    private int carbsGram;
    private int fatGram;

    // Phần Water Intake
    private int waterIntakeMl; // Lượng nước khuyến nghị (ml/ngày)

    // Phần Sleep Goals (NEW)
    private Double sleepTargetHours; // Số giờ ngủ mục tiêu
    private String sleepBedtimeTarget; // Giờ đi ngủ lý tưởng (HH:mm format)
    private String sleepWakeTimeTarget; // Giờ thức dậy lý tưởng (HH:mm format)
}
