package com.wello.wellobackend.dto.requests;

import com.wello.wellobackend.enums.ActivityLevel;
import com.wello.wellobackend.enums.Gender;
import com.wello.wellobackend.enums.Goal;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
//du lieu tu survey nguoi dung gui ve
public class SurveyRequest {
    private int userId;
    private String fullname;
    private Gender gender;
    private int age;
    private int height;
    private int weight;
    private Goal goal;
    private ActivityLevel activityLevel;
}
