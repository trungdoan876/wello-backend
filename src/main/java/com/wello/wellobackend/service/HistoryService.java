package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.HistoryResponse;

import java.util.List;

public interface HistoryService {
    /**
     * Lấy lịch sử tất cả bản ghi của một người dùng
     * 
     * @param userId ID của người dùng
     * @return Danh sách lịch sử
     */
    List<HistoryResponse> getHistoryByUserId(int userId);

    /**
     * Lấy lịch sử gần nhất
     * 
     * @param userId ID của người dùng
     * @param limit  Số lượng bản ghi
     * @return Danh sách lịch sử
     */
    List<HistoryResponse> getLatestHistory(int userId, int limit);

    /**
     * Thêm bản ghi lịch sử mới
     * 
     * @param userId        ID của người dùng
     * @param weight        Cân nặng
     * @param height        Chiều cao
     * @param goal          Mục tiêu
     * @param activityLevel Mức độ hoạt động
     * @return Bản ghi lịch sử vừa tạo
     */
    HistoryResponse addHistory(int userId, double weight, int height,
            String goal, String activityLevel);

    /**
     * Xóa bản ghi lịch sử
     * 
     * @param historyId ID của bản ghi lịch sử
     */
    void deleteHistory(int historyId);

    /**
     * Cập nhật bản ghi lịch sử
     * 
     * @param historyId ID của bản ghi lịch sử
     * @param weight    Cân nặng
     * @param height    Chiều cao
     * @return Bản ghi lịch sử đã cập nhật
     */
    HistoryResponse updateHistory(int historyId, double weight, int height);
}
