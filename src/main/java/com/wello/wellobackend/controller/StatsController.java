package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.responses.SevenDayStatsResponse;
import com.wello.wellobackend.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    /**
     * Lấy thống kê 7 ngày gần nhất
     * 
     * @param userId ID của người dùng
     * @return SevenDayStatsResponse với:
     *         - daysWithSufficientWater: Số ngày uống đủ nước
     *         - daysWithWorkout: Số ngày tập luyện
     *         - daysExceedingCalories: Số ngày vượt calo
     *         - totalDays: Tổng số ngày (7)
     */
    @GetMapping("/seven-days/{userId}")
    public ResponseEntity<?> getSevenDayStats(@PathVariable int userId) {
        try {
            SevenDayStatsResponse response = statsService.getSevenDayStats(userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Get seven day stats error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to get seven day stats");
        }
    }
}
