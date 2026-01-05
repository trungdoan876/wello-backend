package com.wello.wellobackend.service;

import com.wello.wellobackend.model.DailyStreak;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.DailyStreakRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class StreakServiceImpl implements StreakService {

    private final DailyStreakRepository repo;

    public StreakServiceImpl(DailyStreakRepository repo) {
        this.repo = repo;
    }

    @Override
    public void recordWater(User user, LocalDate date) {
        DailyStreak streak = repo.findByUserAndDate(user, date)
                .orElse(DailyStreak.builder()
                        .user(user)
                        .date(date)
                        .build());

        if (!streak.isWater()) {
            streak.setWater(true);
            repo.save(streak);
        }
    }

    @Override
    public void recordMeal(User user, LocalDate date) {
        DailyStreak streak = repo.findByUserAndDate(user, date)
                .orElse(DailyStreak.builder()
                        .user(user)
                        .date(date)
                        .build());

        if (!streak.isMeal()) {
            streak.setMeal(true);
            repo.save(streak);
        }
    }

    @Override
    public List<DailyStreak> getMonthlyStreak(User user, int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        return repo.findAllByUserAndDateBetween(
                user,
                ym.atDay(1),
                ym.atEndOfMonth()
        );
    }
}
