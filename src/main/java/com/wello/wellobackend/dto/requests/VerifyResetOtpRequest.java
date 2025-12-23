package com.wello.wellobackend.dto.requests;

import lombok.Data;

@Data
public class VerifyResetOtpRequest {
    private String email;
    private String otp;
    private String verificationToken;
}
