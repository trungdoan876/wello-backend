package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
//dung khi lay danh sach cau hoi
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponse {
    private int id;
    private String key;         // ACTIVITY
    private String type;        // SELECTION
    private String question;       // Câu hỏi

    // Config (có thể null nếu là câu hỏi Text/Selection)
//    private Double min;
//    private Double max;
//    private Double defaultValue;
    private String unit;
//    private String placeholder;

    private List<AnswerOptionResponse> options;
}
