package com.wello.wellobackend.dto.responses;

import lombok.*;
//response dung cho login, register
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private boolean success;
    private String message;
    private int id_user;
    private boolean hasCompletedSurvey;
}
