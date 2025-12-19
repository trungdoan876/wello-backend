package com.wello.wellobackend.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wello.wellobackend.enums.ActivityLevel;
import com.wello.wellobackend.enums.Gender;
import com.wello.wellobackend.enums.Goal;
import com.wello.wellobackend.enums.AuthProvider;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfoResponse {
    private int userId;
    private String email;
    private AuthProvider authProvider;
    private String googleId;
    private String fcmToken;

    // Profile fields (nullable if profile not created yet)
    private String fullname;
    private Gender gender;
    private Integer age;
    private Integer height;
    private Integer weight;
    private Goal goal;
    private ActivityLevel activityLevel;
    private String avatarUrl;
    private LocalDateTime surveyDate;
}
