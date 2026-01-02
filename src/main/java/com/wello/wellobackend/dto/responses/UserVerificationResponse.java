package com.wello.wellobackend.dto.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVerificationResponse {
    private boolean exists;
    private Boolean valid; // Kiểm tra userId và email có khớp không
    private Integer userId;
    private String message;
}
