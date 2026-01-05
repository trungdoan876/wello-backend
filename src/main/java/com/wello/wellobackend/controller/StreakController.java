package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.DailyStreakResponse;
import com.wello.wellobackend.model.DailyStreak;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.AuthRepository;
import com.wello.wellobackend.service.StreakService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/streaks")
@CrossOrigin
public class StreakController {

    private final StreakService streakService;
    private final AuthRepository authRepository;

    public StreakController(
            StreakService streakService,
            AuthRepository authRepository
    ) {
        this.streakService = streakService;
        this.authRepository = authRepository;
    }

    /**
     * GET /api/streaks/monthly?userId=1&year=2026&month=1
     */
    @GetMapping("/monthly")
    public List<DailyStreakResponse> getMonthlyStreak(
            @RequestParam int userId,
            @RequestParam int year,
            @RequestParam int month
    ) {
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        return streakService.getMonthlyStreak(user, year, month)
                .stream()
                .map(s -> new DailyStreakResponse(
                        s.getDate(),
                        s.isWater(),
                        s.isMeal()
                ))
                .toList();
    }
}
