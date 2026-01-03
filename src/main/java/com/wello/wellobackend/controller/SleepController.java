package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.LogBedtimeRequest;
import com.wello.wellobackend.dto.requests.CompleteSleepRequest;
import com.wello.wellobackend.dto.requests.UpdateSleepTrackerRequest;
import com.wello.wellobackend.dto.responses.SleepTrackerResponse;
import com.wello.wellobackend.dto.responses.TodaySleepResponse;
import com.wello.wellobackend.service.SleepService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * REST API Controller for Sleep Tracking - Quick Log + Basic CRUD
 */
@RestController
@RequestMapping("/api/sleep")
@RequiredArgsConstructor
public class SleepController {

    private final SleepService sleepService;

    // ========== Quick Log Endpoints ==========

    /**
     * POST /api/sleep/log-bedtime - Ghi giờ đi ngủ (Quick Log Step 1)
     */
    @PostMapping("/log-bedtime")
    public ResponseEntity<SleepTrackerResponse> logBedtime(
            @RequestBody LogBedtimeRequest request) {
        return ResponseEntity.ok(sleepService.logBedtime(request.getUserId(), request.getBedtime()));
    }

    /**
     * PUT /api/sleep/complete - Ghi giờ thức dậy (Quick Log Step 2)
     */
    @PutMapping("/complete")
    public ResponseEntity<SleepTrackerResponse> completeSleep(
            @RequestBody CompleteSleepRequest request) {
        return ResponseEntity.ok(sleepService.completeSleep(
                request.getUserId(), request.getWakeTime(), request.getQuality()));
    }

    /**
     * GET /api/sleep/today - Lấy giấc ngủ hôm nay
     */
    @GetMapping("/today")
    public ResponseEntity<TodaySleepResponse> getTodaySleep(
            @RequestParam int userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(sleepService.getTodaySleep(userId, date));
    }

    // ========== Update & Delete Endpoints ==========

    /**
     * PUT /api/sleep/tracker/{id} - Cập nhật sleep tracker
     */
    @PutMapping("/tracker/{id}")
    public ResponseEntity<SleepTrackerResponse> updateSleepTracker(
            @PathVariable int id,
            @RequestBody UpdateSleepTrackerRequest request) {
        return ResponseEntity.ok(sleepService.updateSleepTracker(request.getUserId(), id, request));
    }

    /**
     * DELETE /api/sleep/tracker/{id} - Xóa sleep tracker
     */
    @DeleteMapping("/tracker/{id}")
    public ResponseEntity<Void> deleteSleepTracker(
            @PathVariable int id,
            @RequestParam int userId) {
        sleepService.deleteSleepTracker(userId, id);
        return ResponseEntity.ok().build();
    }
}
