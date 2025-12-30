package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.AddCustomFoodRequest;
import com.wello.wellobackend.dto.requests.AddFavoriteFoodRequest;
import com.wello.wellobackend.dto.requests.FoodItemRequest;
import com.wello.wellobackend.dto.requests.LogFavoriteRequest;
import com.wello.wellobackend.dto.requests.UpdateCustomFoodRequest;
import com.wello.wellobackend.dto.requests.UpdateFavoriteFoodRequest;
import com.wello.wellobackend.dto.responses.CustomFoodResponse;
import com.wello.wellobackend.dto.responses.FavoriteFoodDetailResponse;
import com.wello.wellobackend.dto.responses.FoodItemDetail;
import com.wello.wellobackend.dto.responses.LogFoodResponse;
import com.wello.wellobackend.dto.responses.NutritionSummary;
import com.wello.wellobackend.enums.MealType;
import com.wello.wellobackend.model.*;
import com.wello.wellobackend.model.FavoriteItem;
import com.wello.wellobackend.model.Food;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.repository.FavoriteItemRepository;
import com.wello.wellobackend.repository.FavoriteRepository;
import com.wello.wellobackend.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

        @Autowired
        private FavoriteRepository favoriteRepository;

        @Autowired
        private FavoriteItemRepository favoriteItemRepository;

        @Autowired
        private FoodRepository foodRepository;

        @Autowired
        private AuthRepository authRepository;

        @Autowired
        private com.wello.wellobackend.repository.NutritionTrackerRepository nutritionTrackerRepository;

        @Override
        public List<FavoriteFoodDetailResponse> getFavoritesByUser(int userId) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                return favoriteRepository.findByUser(user).stream()
                                .map(this::mapToFoodDetailResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public FavoriteFoodDetailResponse getFavoriteById(int userId, int favoriteId) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                Favorite favorite = favoriteRepository.findByIdAndUser(favoriteId, user)
                                .orElseThrow(() -> new RuntimeException("Món ăn yêu thích không tồn tại"));

                return mapToFoodDetailResponse(favorite);
        }

        @Override
        @Transactional
        public FavoriteFoodDetailResponse addFavoriteFood(AddFavoriteFoodRequest request) {
                try {
                        System.out.println("=== DEBUG addFavoriteFood ===");
                        System.out.println("Request: " + request);

                        User user = authRepository.findById(request.getUserId())
                                        .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
                        System.out.println("User found: " + user.getIdUser());

                        // Create favorite
                        Favorite favorite = new Favorite();
                        favorite.setUser(user);
                        favorite.setFavoriteName(request.getFavoriteName());
                        favorite.setMealType(request.getMealType());

                        // Save favorite first to get ID
                        Favorite savedFavorite = favoriteRepository.save(favorite);
                        System.out.println("Favorite saved: " + savedFavorite.getId());

                        // Create and add items
                        List<FavoriteItem> items = new ArrayList<>();
                        for (FoodItemRequest itemReq : request.getItems()) {
                                Food food = foodRepository.findById(itemReq.getFoodId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Món ăn không tồn tại: " + itemReq.getFoodId()));

                                FavoriteItem item = new FavoriteItem();
                                item.setFavorite(savedFavorite);
                                item.setFood(food);
                                item.setAmountGrams(itemReq.getAmountGrams());
                                items.add(item);
                        }

                        favoriteItemRepository.saveAll(items);
                        savedFavorite.setItems(items);

                        return mapToFoodDetailResponse(savedFavorite);
                } catch (Exception e) {
                        System.err.println("ERROR: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Lỗi: " + e.getMessage(), e);
                }
        }

        @Override
        @Transactional
        public FavoriteFoodDetailResponse updateFavoriteFood(UpdateFavoriteFoodRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                Favorite favorite = favoriteRepository.findByIdAndUser(request.getFavoriteId(), user)
                                .orElseThrow(() -> new RuntimeException("Món ăn yêu thích không tồn tại"));

                // Update basic info
                favorite.setFavoriteName(request.getFavoriteName());
                favorite.setMealType(request.getMealType());

                // Clear old items (orphanRemoval will delete them)
                favorite.getItems().clear();

                // Create new items
                for (FoodItemRequest itemReq : request.getItems()) {
                        Food food = foodRepository.findById(itemReq.getFoodId())
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Món ăn không tồn tại: " + itemReq.getFoodId()));

                        FavoriteItem item = new FavoriteItem();
                        item.setFavorite(favorite);
                        item.setFood(food);
                        item.setAmountGrams(itemReq.getAmountGrams());
                        favorite.getItems().add(item);
                }

                Favorite updatedFavorite = favoriteRepository.save(favorite);
                return mapToFoodDetailResponse(updatedFavorite);
        }

        @Override
        @Transactional
        public CustomFoodResponse addCustomFood(AddCustomFoodRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                Food food = new Food();
                food.setFoodName(request.getFoodName());
                food.setCaloriesPer100g(request.getCaloriesPer100g());
                food.setProteinPer100g(request.getProteinPer100g());
                food.setCarbsPer100g(request.getCarbsPer100g());
                food.setFatPer100g(request.getFatPer100g());
                foodRepository.save(food);

                Favorite favorite = new Favorite();
                favorite.setUser(user);
                favorite.setFavoriteName(request.getFoodName());
                favorite.setMealType(request.getMealType());
                favoriteRepository.save(favorite);

                FavoriteItem item = new FavoriteItem();
                item.setFavorite(favorite);
                item.setFood(food);
                item.setAmountGrams(100); // Mặc định cho custom food là 100g
                favoriteItemRepository.save(item);

                return CustomFoodResponse.builder()
                                .id(favorite.getId())
                                .foodName(food.getFoodName())
                                .calories(food.getCaloriesPer100g())
                                .protein(food.getProteinPer100g())
                                .carbs(food.getCarbsPer100g())
                                .fat(food.getFatPer100g())
                                .mealType(favorite.getMealType())
                                .build();
        }

        @Override
        @Transactional
        public CustomFoodResponse updateCustomFood(UpdateCustomFoodRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                Favorite favorite = favoriteRepository.findByIdAndUser(request.getFavoriteId(), user)
                                .orElseThrow(() -> new RuntimeException("Món ăn tùy chỉnh không tồn tại"));

                // Với CustomFood, Favorite chỉ có 1 item
                if (favorite.getItems().isEmpty()) {
                        throw new RuntimeException("Lỗi dữ liệu: Món ăn tùy chỉnh không có thực phẩm đi kèm");
                }

                FavoriteItem firstItem = favorite.getItems().get(0);
                Food food = firstItem.getFood();

                // Cập nhật thông tin món ăn
                food.setFoodName(request.getFoodName());
                food.setCaloriesPer100g(request.getCaloriesPer100g());
                food.setProteinPer100g(request.getProteinPer100g());
                food.setCarbsPer100g(request.getCarbsPer100g());
                food.setFatPer100g(request.getFatPer100g());
                foodRepository.save(food);

                // Cập nhật thông tin Favorite
                favorite.setFavoriteName(request.getFoodName());
                favorite.setMealType(request.getMealType());
                favoriteRepository.save(favorite);

                return CustomFoodResponse.builder()
                                .id(favorite.getId())
                                .foodName(food.getFoodName())
                                .calories(food.getCaloriesPer100g())
                                .protein(food.getProteinPer100g())
                                .carbs(food.getCarbsPer100g())
                                .fat(food.getFatPer100g())
                                .mealType(favorite.getMealType())
                                .build();
        }

        @Override
        @Transactional
        public void deleteFavorite(int userId, int favoriteId) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                favoriteRepository.deleteByIdAndUser(favoriteId, user);
        }

        @Override
        public List<FavoriteFoodDetailResponse> searchFavorites(int userId, String query) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                return favoriteRepository.findByUserAndFavoriteNameContainingIgnoreCase(user, query).stream()
                                .map(this::mapToFoodDetailResponse)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public LogFoodResponse logFavorite(LogFavoriteRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                Favorite favorite = favoriteRepository.findByIdAndUser(request.getFavoriteId(), user)
                                .orElseThrow(() -> new RuntimeException("Món ăn yêu thích không tồn tại"));

                MealType mealType = request.getMealType() != null ? request.getMealType() : favorite.getMealType();
                if (mealType == null)
                        mealType = MealType.BREAKFAST; // Mặc định

                int totalCalories = 0;
                double totalProtein = 0;
                double totalCarbs = 0;
                double totalFat = 0;

                java.time.LocalDateTime logTime = java.time.LocalDateTime.of(request.getDate(),
                                java.time.LocalTime.now());

                for (FavoriteItem item : favorite.getItems()) {
                        Food food = item.getFood();
                        double factor = item.getAmountGrams() / 100.0;

                        int calories = (int) (food.getCaloriesPer100g() * factor);
                        double protein = food.getProteinPer100g() * factor;
                        double carbs = food.getCarbsPer100g() * factor;
                        double fat = food.getFatPer100g() * factor;

                        NutritionTracker tracker = new NutritionTracker();
                        tracker.setUser(user);
                        tracker.setDate(logTime);
                        tracker.setFood(food);
                        tracker.setAmountGrams(item.getAmountGrams());
                        tracker.setMealType(mealType);
                        tracker.setCaloriesConsumed(calories);
                        tracker.setProtein((int) Math.round(protein));
                        tracker.setCarbs((int) Math.round(carbs));
                        tracker.setFat((int) Math.round(fat));
                        tracker.setCaloriesBurned(0);
                        tracker.setFavoriteName(favorite.getFavoriteName());

                        nutritionTrackerRepository.save(tracker);

                        totalCalories += calories;
                        totalProtein += protein;
                        totalCarbs += carbs;
                        totalFat += fat;
                }

                return LogFoodResponse.builder()
                                .foodName(favorite.getFavoriteName())
                                .calories(totalCalories)
                                .protein(Math.round(totalProtein * 10) / 10.0)
                                .carbs(Math.round(totalCarbs * 10) / 10.0)
                                .fat(Math.round(totalFat * 10) / 10.0)
                                .message("Đã ghi nhận " + favorite.getFavoriteName() + " vào nhật ký dinh dưỡng cho "
                                                + mealType)
                                .build();
        }

        private FavoriteFoodDetailResponse mapToFoodDetailResponse(Favorite favorite) {
                List<FoodItemDetail> itemDetails = new ArrayList<>();
                int totalCalories = 0;
                double totalProtein = 0;
                double totalCarbs = 0;
                double totalFat = 0;

                for (FavoriteItem item : favorite.getItems()) {
                        Food food = item.getFood();
                        double multiplier = item.getAmountGrams() / 100.0;

                        int calories = (int) (food.getCaloriesPer100g() * multiplier);
                        double protein = food.getProteinPer100g() * multiplier;
                        double carbs = food.getCarbsPer100g() * multiplier;
                        double fat = food.getFatPer100g() * multiplier;

                        itemDetails.add(FoodItemDetail.builder()
                                        .itemId(item.getId())
                                        .foodId(food.getId())
                                        .foodName(food.getFoodName())
                                        .amountGrams(item.getAmountGrams())
                                        .calories(calories)
                                        .protein(Math.round(protein * 10) / 10.0)
                                        .carbs(Math.round(carbs * 10) / 10.0)
                                        .fat(Math.round(fat * 10) / 10.0)
                                        .build());

                        totalCalories += calories;
                        totalProtein += protein;
                        totalCarbs += carbs;
                        totalFat += fat;
                }

                NutritionSummary nutritionSummary = NutritionSummary
                                .builder()
                                .totalCalories(totalCalories)
                                .totalProtein(Math.round(totalProtein * 10) / 10.0)
                                .totalCarbs(Math.round(totalCarbs * 10) / 10.0)
                                .totalFat(Math.round(totalFat * 10) / 10.0)
                                .build();

                return FavoriteFoodDetailResponse.builder()
                                .id(favorite.getId())
                                .favoriteName(favorite.getFavoriteName())
                                .mealType(favorite.getMealType())
                                .items(itemDetails)
                                .totalNutrition(nutritionSummary)
                                .createdAt(favorite.getCreatedAt())
                                .build();
        }
}
