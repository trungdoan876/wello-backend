package com.wello.wellobackend.dto.requests;

import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
}
