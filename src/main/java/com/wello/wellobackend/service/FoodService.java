package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.FoodResponse;
import com.wello.wellobackend.dto.responses.FoodPreviewResponse;
import com.wello.wellobackend.dto.requests.FoodPreviewRequest;
import java.util.List;

public interface FoodService {
    List<FoodResponse> getAllFoods();

    List<FoodResponse> searchFoods(String query);

    FoodPreviewResponse previewFood(FoodPreviewRequest request);
}
