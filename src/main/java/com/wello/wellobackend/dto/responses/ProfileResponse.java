package com.wello.wellobackend.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    private int idProfile;
    private int userId;
    private String fullname;
    private String gender;
    private int age;
    private int height;
    private int weight;
    private String goal;
    private String activityLevel;
    private String avatarUrl;
    private LocalDateTime surveyDate;
}
