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
public class UpdateFavoriteComboRequest {
    private int userId;
    private int favoriteId;
    private String favoriteName;
    private MealType mealType;
    private List<AddFavoriteComboRequest.FoodItemRequest> items;
}
