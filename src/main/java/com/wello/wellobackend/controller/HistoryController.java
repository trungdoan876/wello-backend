package com.wello.wellobackend.controller;

import com.wello.wellobackend.dto.requests.HistoryRequest;
import com.wello.wellobackend.dto.responses.HistoryResponse;
import com.wello.wellobackend.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    /**
     * Lấy tất cả lịch sử của người dùng
     * GET /api/history/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<HistoryResponse>> getHistory(@PathVariable int userId) {
        List<HistoryResponse> histories = historyService.getHistoryByUserId(userId);
        return ResponseEntity.ok(histories);
    }

    /**
     * Lấy N bản ghi lịch sử gần nhất
     * GET /api/history/{userId}/latest?limit=5
     */
    @GetMapping("/{userId}/latest")
    public ResponseEntity<List<HistoryResponse>> getLatestHistory(
            @PathVariable int userId,
            @RequestParam(defaultValue = "5") int limit) {
        List<HistoryResponse> histories = historyService.getLatestHistory(userId, limit);
        return ResponseEntity.ok(histories);
    }

    /**
     * Thêm bản ghi lịch sử mới
     * POST /api/history/{userId}
     */
    @PostMapping("/{userId}")
    public ResponseEntity<HistoryResponse> addHistory(
            @PathVariable int userId,
            @RequestBody HistoryRequest request) {
        HistoryResponse response = historyService.addHistory(
                userId,
                request.getWeight(),
                request.getHeight(),
                request.getGoal(),
                request.getActivityLevel()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Cập nhật bản ghi lịch sử
     * PUT /api/history/{historyId}
     */
    @PutMapping("/{historyId}")
    public ResponseEntity<HistoryResponse> updateHistory(
            @PathVariable int historyId,
            @RequestBody HistoryRequest request) {
        HistoryResponse response = historyService.updateHistory(
                historyId,
                request.getWeight(),
                request.getHeight()
        );
        return ResponseEntity.ok(response);
    }

    /**
     * Xóa bản ghi lịch sử
     * DELETE /api/history/{historyId}
     */
    @DeleteMapping("/{historyId}")
    public ResponseEntity<Void> deleteHistory(@PathVariable int historyId) {
        historyService.deleteHistory(historyId);
        return ResponseEntity.noContent().build();
    }
}
