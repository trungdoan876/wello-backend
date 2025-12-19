package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.FoodResponse;
import java.util.List;

public interface FoodService {
    List<FoodResponse> getAllFoods();

    List<FoodResponse> searchFoods(String query);
}
