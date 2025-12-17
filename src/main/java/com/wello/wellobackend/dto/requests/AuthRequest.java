package com.wello.wellobackend.dto.requests;

import lombok.*;

//dung khi login/register
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String email;
    private String password;
}
