package com.wello.wellobackend.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvatarUploadRequest {
    private String base64Image; // Base64 encoded image string
}
