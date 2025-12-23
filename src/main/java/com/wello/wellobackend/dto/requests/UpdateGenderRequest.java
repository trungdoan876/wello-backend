package com.wello.wellobackend.dto.requests;

import com.wello.wellobackend.enums.Gender;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateGenderRequest {
    private Gender gender;
}
