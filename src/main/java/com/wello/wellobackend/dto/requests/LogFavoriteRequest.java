package com.wello.wellobackend.dto.requests;

import com.wello.wellobackend.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogFavoriteRequest {
    private int userId;
    private int favoriteId;
    private LocalDate date;
    private MealType mealType; // Nếu null sẽ dùng mealType mặc định của món yêu thích
}
