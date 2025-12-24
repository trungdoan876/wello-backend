package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.AddFavoriteComboRequest;
import com.wello.wellobackend.dto.requests.UpdateFavoriteComboRequest;
import com.wello.wellobackend.dto.responses.FavoriteComboResponse;
import com.wello.wellobackend.model.Favorite;
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

        @Override
        public List<FavoriteComboResponse> getFavoritesByUser(int userId) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                return favoriteRepository.findByUser(user).stream()
                                .map(this::mapToComboResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public FavoriteComboResponse getFavoriteById(int userId, int favoriteId) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                Favorite favorite = favoriteRepository.findByIdAndUser(favoriteId, user)
                                .orElseThrow(() -> new RuntimeException("Món ăn yêu thích không tồn tại"));

                return mapToComboResponse(favorite);
        }

        @Override
        @Transactional
        public FavoriteComboResponse addFavoriteCombo(AddFavoriteComboRequest request) {
                try {
                        System.out.println("=== DEBUG addFavoriteCombo ===");
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
                        for (AddFavoriteComboRequest.FoodItemRequest itemReq : request.getItems()) {
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

                        return mapToComboResponse(savedFavorite);
                } catch (Exception e) {
                        System.err.println("ERROR: " + e.getMessage());
                        e.printStackTrace();
                        throw new RuntimeException("Lỗi: " + e.getMessage(), e);
                }
        }

        @Override
        @Transactional
        public FavoriteComboResponse updateFavoriteCombo(UpdateFavoriteComboRequest request) {
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
                for (AddFavoriteComboRequest.FoodItemRequest itemReq : request.getItems()) {
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
                return mapToComboResponse(updatedFavorite);
        }

        @Override
        @Transactional
        public void deleteFavorite(int userId, int favoriteId) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                favoriteRepository.deleteByIdAndUser(favoriteId, user);
        }

        @Override
        public List<FavoriteComboResponse> searchFavorites(int userId, String query) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

                return favoriteRepository.findByUserAndFavoriteNameContainingIgnoreCase(user, query).stream()
                                .map(this::mapToComboResponse)
                                .collect(Collectors.toList());
        }

        private FavoriteComboResponse mapToComboResponse(Favorite favorite) {
                List<FavoriteComboResponse.FoodItemDetail> itemDetails = new ArrayList<>();
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

                        itemDetails.add(FavoriteComboResponse.FoodItemDetail.builder()
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

                FavoriteComboResponse.NutritionSummary nutritionSummary = FavoriteComboResponse.NutritionSummary
                                .builder()
                                .totalCalories(totalCalories)
                                .totalProtein(Math.round(totalProtein * 10) / 10.0)
                                .totalCarbs(Math.round(totalCarbs * 10) / 10.0)
                                .totalFat(Math.round(totalFat * 10) / 10.0)
                                .build();

                return FavoriteComboResponse.builder()
                                .id(favorite.getId())
                                .favoriteName(favorite.getFavoriteName())
                                .mealType(favorite.getMealType())
                                .items(itemDetails)
                                .totalNutrition(nutritionSummary)
                                .createdAt(favorite.getCreatedAt())
                                .build();
        }
}
