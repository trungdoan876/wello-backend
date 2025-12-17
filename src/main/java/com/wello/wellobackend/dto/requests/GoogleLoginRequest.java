package com.wello.wellobackend.dto.requests;

import lombok.*;

// Request for Google Sign-In
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginRequest {
    private String idToken; // Google ID Token from Flutter
}
