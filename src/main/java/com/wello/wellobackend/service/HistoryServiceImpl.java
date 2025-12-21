package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.responses.HistoryResponse;
import com.wello.wellobackend.enums.ActivityLevel;
import com.wello.wellobackend.enums.Goal;
import com.wello.wellobackend.exception.ResourceNotFoundException;
import com.wello.wellobackend.model.History;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.HistoryRepository;
import com.wello.wellobackend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class HistoryServiceImpl implements HistoryService {

        @Autowired
        private HistoryRepository historyRepository;

        @Autowired
        private UserRepository userRepository;

        @Override
        public List<HistoryResponse> getHistoryByUserId(int userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Người dùng với ID " + userId + " không tồn tại"));

                return historyRepository.findByUserOrderByRecordedAtDesc(user)
                                .stream()
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public List<HistoryResponse> getLatestHistory(int userId, int limit) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Người dùng với ID " + userId + " không tồn tại"));

                return historyRepository.findByUserOrderByRecordedAtDesc(user)
                                .stream()
                                .limit(limit)
                                .map(this::convertToResponse)
                                .collect(Collectors.toList());
        }

        @Override
        public HistoryResponse addHistory(int userId, double weight, int height,
                        String goal, String activityLevel) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Người dùng với ID " + userId + " không tồn tại"));

                History history = History.builder()
                                .user(user)
                                .weight(weight)
                                .height(height)
                                .goal(Goal.valueOf(goal.toUpperCase()))
                                .activityLevel(ActivityLevel.valueOf(activityLevel.toUpperCase()))
                                .recordedAt(LocalDateTime.now())
                                .build();

                History savedHistory = historyRepository.save(history);
                return convertToResponse(savedHistory);
        }

        @Override
        public void deleteHistory(int historyId) {
                History history = historyRepository.findById(historyId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Bản ghi lịch sử với ID " + historyId + " không tồn tại"));

                historyRepository.delete(history);
        }

        @Override
        public HistoryResponse updateHistory(int historyId, double weight, int height) {
                History history = historyRepository.findById(historyId)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Bản ghi lịch sử với ID " + historyId + " không tồn tại"));

                history.setWeight(weight);
                history.setHeight(height);

                History updatedHistory = historyRepository.save(history);
                return convertToResponse(updatedHistory);
        }

        private HistoryResponse convertToResponse(History history) {
                return HistoryResponse.builder()
                                .id(history.getId())
                                .userId(history.getUser().getIdUser())
                                .weight(history.getWeight())
                                .height(history.getHeight())
                                .goal(history.getGoal())
                                .activityLevel(history.getActivityLevel())
                                .recordedAt(history.getRecordedAt())
                                .build();
        }
}
