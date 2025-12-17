package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerOptionResponse {
    private String answer;  // Code từ DB ("SEDENTARY")
    private String moTa; // Lấy từ ENUM ("Ít vận động")
//    private String desc;  // Lấy từ ENUM ("Ngồi nhiều...")
}
