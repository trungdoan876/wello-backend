package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.DailyNutritionResponse;
import com.wello.wellobackend.model.Target;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.model.WaterTracker;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.repository.TargetRepository;
import com.wello.wellobackend.repository.WaterTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class WaterTrackerServiceImpl implements WaterTrackerService {

        @Autowired
        private AuthRepository authRepository;

        @Autowired
        private WaterTrackerRepository waterTrackerRepository;

        @Autowired
        private TargetRepository targetRepository;

        @Override
        public DailyNutritionResponse.WaterIntake getDailyWaterIntake(int userId, LocalDate date) {
                User user = authRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                LocalDateTime startOfDay = LocalDateTime.of(date, LocalTime.MIN);
                LocalDateTime endOfDay = LocalDateTime.of(date, LocalTime.MAX);

                WaterTracker water = waterTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElse(new WaterTracker());

                Target target = targetRepository.findByUser(user);

                return DailyNutritionResponse.WaterIntake.builder()
                                .consumed(water.getAmountMl())
                                .target(target != null ? target.getWaterIntakeMl() : 0)
                                .unit("ml")
                                .glasses(water.getAmountMl() / 250)
                                .build();
        }

        @Override
        public void addWaterIntake(com.wello.wellobackend.dto.requests.AddWaterIntakeRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                LocalDateTime startOfDay = LocalDateTime.of(request.getDate(), LocalTime.MIN);
                LocalDateTime endOfDay = LocalDateTime.of(request.getDate(), LocalTime.MAX);

                WaterTracker water = waterTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElse(null);

                if (water == null) {
                        water = new WaterTracker();
                        water.setUser(user);
                        water.setDate(request.getDate().atStartOfDay());
                        water.setAmountMl(request.getAmountMl());
                } else {
                        water.setAmountMl(water.getAmountMl() + request.getAmountMl());
                }

                waterTrackerRepository.save(water);
        }

        @Override
        public void deleteWaterIntake(com.wello.wellobackend.dto.requests.AddWaterIntakeRequest request) {
                User user = authRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("User not found"));

                LocalDateTime startOfDay = LocalDateTime.of(request.getDate(), LocalTime.MIN);
                LocalDateTime endOfDay = LocalDateTime.of(request.getDate(), LocalTime.MAX);

                WaterTracker water = waterTrackerRepository.findByUserAndDate(user, startOfDay, endOfDay)
                                .orElse(null);

                if (water == null) {
                        throw new RuntimeException("Water intake record not found for the given date");
                } else {
                        int newAmount = water.getAmountMl() - 250;
                        if (newAmount < 0) {
                                newAmount = 0;
                        }
                        water.setAmountMl(newAmount);
                }

                waterTrackerRepository.save(water);
        }

        @Override
        public void deleteWaterIntakeById(int waterTrackerId) {
                WaterTracker water = waterTrackerRepository.findById(waterTrackerId)
                                .orElseThrow(() -> new RuntimeException("Water intake record not found"));

                waterTrackerRepository.delete(water);
        }
}
