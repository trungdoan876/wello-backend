package com.wello.wellobackend.dto.requests;

import com.wello.wellobackend.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddFavoriteComboRequest {
    private int userId;
    private String favoriteName;
    private MealType mealType;
    private List<FoodItemRequest> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FoodItemRequest {
        private int foodId;
        private int amountGrams;
    }
}
