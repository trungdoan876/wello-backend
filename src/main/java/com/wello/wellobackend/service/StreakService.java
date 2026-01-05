package com.wello.wellobackend.service;

import com.wello.wellobackend.model.DailyStreak;
import com.wello.wellobackend.model.User;

import java.time.LocalDate;
import java.util.List;

public interface StreakService {

    void recordWater(User user, LocalDate date);

    void recordMeal(User user, LocalDate date);

    List<DailyStreak> getMonthlyStreak(User user, int year, int month);
}
