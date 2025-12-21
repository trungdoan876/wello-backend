package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.FoodPreviewRequest;
import com.wello.wellobackend.dto.responses.FoodPreviewResponse;
import com.wello.wellobackend.dto.responses.FoodResponse;
import com.wello.wellobackend.model.Food;
import com.wello.wellobackend.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

        @Autowired
        private FoodRepository foodRepository;

        @Override
        public List<FoodResponse> getAllFoods() {
                return foodRepository.findAll().stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<FoodResponse> searchFoods(String query) {
                return foodRepository.findByFoodNameContainingIgnoreCase(query).stream()
                                .map(this::mapToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public FoodPreviewResponse previewFood(
                        FoodPreviewRequest request) {
                Food food = foodRepository.findById(request.getFoodId())
                                .orElseThrow(() -> new RuntimeException("Không tìm thấy thực phẩm"));

                double factor = request.getAmountGrams() / 100.0;

                return FoodPreviewResponse.builder()
                                .id(food.getId())
                                .name(food.getFoodName())
                                .calories((int) (food.getCaloriesPer100g() * factor))
                                .protein(food.getProteinPer100g() * factor)
                                .carbs(food.getCarbsPer100g() * factor)
                                .fat(food.getFatPer100g() * factor)
                                .build();
        }

        private FoodResponse mapToResponse(Food food) {
                return FoodResponse.builder()
                                .id(food.getId())
                                .name(food.getFoodName())
                                .calories(food.getCaloriesPer100g())
                                .protein(food.getProteinPer100g())
                                .carbs(food.getCarbsPer100g())
                                .fat(food.getFatPer100g())
                                .imageUrl(food.getImageUrl())
                                .build();
        }
}
