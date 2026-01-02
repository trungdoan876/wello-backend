package com.wello.wellobackend.service;

import com.wello.wellobackend.dto.requests.UpdateSleepTrackerRequest;
import com.wello.wellobackend.dto.responses.*;
import com.wello.wellobackend.exception.ResourceNotFoundException;
import com.wello.wellobackend.model.Profile;
import com.wello.wellobackend.model.SleepTracker;
import com.wello.wellobackend.model.User;
import com.wello.wellobackend.repository.ProfileRepository;
import com.wello.wellobackend.repository.SleepTrackerRepository;
import com.wello.wellobackend.repository.UserRepository;
import com.wello.wellobackend.enums.SleepStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Implementation của Sleep Service
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SleepServiceImpl implements SleepService {

        private final SleepTrackerRepository sleepTrackerRepository;
        private final UserRepository userRepository;
        private final ProfileRepository profileRepository;
        private final SleepRecommendationService sleepRecommendationService;
        private final SleepAnalysisService sleepAnalysisService;

        @Override
        public SleepTrackerResponse updateSleepTracker(int userId, int trackerId,
                        UpdateSleepTrackerRequest request) {
                // 1. Find existing
                SleepTracker existing = sleepTrackerRepository.findByIdAndUserId(trackerId, (long) userId);
                if (existing == null) {
                        throw new ResourceNotFoundException("Sleep tracker not found");
                }

                // 2. Prepare merged values (handle partial updates)
                LocalDateTime sleepTime = request.getSleepTime() != null ? request.getSleepTime()
                                : existing.getSleepTime();
                LocalDateTime wakeTime = request.getWakeTime() != null ? request.getWakeTime() : existing.getWakeTime();
                Integer quality = request.getQuality() != null ? request.getQuality() : existing.getQuality();
                String notes = request.getNotes() != null ? request.getNotes() : existing.getNotes();

                // 3. Validate
                validateSleepTime(sleepTime, wakeTime);

                // 4. Check date change (logic derived from wakeTime for accuracy)
                LocalDate newDate = wakeTime.toLocalDate();
                LocalDate oldDate = existing.getDate() != null ? existing.getDate().toLocalDate()
                                : (existing.getWakeTime() != null ? existing.getWakeTime().toLocalDate()
                                                : existing.getSleepTime().toLocalDate());

                if (!newDate.equals(oldDate)) {
                        // User đang sửa sang ngày khác
                        // Check conflict (exclude current ID)
                        SleepTracker conflictTracker = sleepTrackerRepository.findByUserIdAndDate((long) userId,
                                        newDate);
                        if (conflictTracker != null && conflictTracker.getId() != trackerId) {
                                throw new IllegalStateException(
                                                "Ngày " + newDate
                                                                + " đã có giấc ngủ khác. Không thể cập nhật.");
                        }
                }

                // 5. Update fields with accurate date based on wakeTime
                existing.setSleepTime(sleepTime);
                existing.setWakeTime(wakeTime);
                existing.setDate(wakeTime.toLocalDate().atStartOfDay()); // Accurate date = wake date
                existing.setQuality(quality);
                existing.setNotes(notes);

                // 6. Recalculate metrics
                calculateMetrics(existing, userId);

                // 7. Save
                SleepTracker updated = sleepTrackerRepository.save(existing);

                return mapToResponse(updated);
        }

        @Override
        public void deleteSleepTracker(int userId, int trackerId) {
                SleepTracker tracker = sleepTrackerRepository.findByIdAndUserId(trackerId, (long) userId);
                if (tracker == null) {
                        throw new ResourceNotFoundException("Sleep tracker not found");
                }

                sleepTrackerRepository.delete(tracker);
        }

        // ========== Helper Methods ==========

        private void validateSleepTime(LocalDateTime sleepTime, LocalDateTime wakeTime) {
                if (sleepTime == null || wakeTime == null) {
                        throw new IllegalArgumentException("Giờ đi ngủ và giờ thức dậy không được để trống");
                }
                if (sleepTime.isAfter(wakeTime) || sleepTime.isEqual(wakeTime)) {
                        throw new IllegalArgumentException("Giờ đi ngủ phải trước giờ thức dậy");
                }

                long minutes = Duration.between(sleepTime, wakeTime).toMinutes();
                if (minutes < 60) {
                        throw new IllegalArgumentException("Giấc ngủ quá ngắn (< 1 giờ)");
                }
                if (minutes > 960) {
                        throw new IllegalArgumentException("Giấc ngủ quá dài (> 16 giờ)");
                }
        }

        private void calculateMetrics(SleepTracker tracker, int userId) {
                // 1. Calculate duration
                long minutes = Duration.between(tracker.getSleepTime(), tracker.getWakeTime()).toMinutes();
                tracker.setDuration((int) minutes);

                // 2. Calculate sleep efficiency
                double efficiency = sleepAnalysisService.calculateSleepEfficiency(tracker);
                tracker.setSleepEfficiency(efficiency);

                // 3. Calculate compliance rate
                Profile profile = profileRepository.findByUser_IdUser(userId);
                if (profile == null) {
                        throw new ResourceNotFoundException("Profile not found");
                }

                SleepRecommendation recommendation = sleepRecommendationService
                                .getRecommendationByAge(profile.getAge());

                double actualHours = minutes / 60.0;
                double recommendedHours = recommendation.getOptimalHours();
                double complianceRate = sleepAnalysisService.calculateComplianceRate(actualHours, recommendedHours);
                tracker.setComplianceRate(complianceRate);
        }

        private SleepTrackerResponse mapToResponse(SleepTracker tracker) {
                return SleepTrackerResponse.builder()
                                .id(tracker.getId())
                                .sleepTime(tracker.getSleepTime())
                                .wakeTime(tracker.getWakeTime())
                                .duration(tracker.getDuration())
                                .durationHours(tracker.getDuration() != null
                                                ? Math.round(tracker.getDuration() / 60.0 * 10.0) / 10.0
                                                : null)
                                .quality(tracker.getQuality())
                                .sleepEfficiency(tracker.getSleepEfficiency())
                                .sleepEfficiencyRating(
                                                tracker.getSleepEfficiency() != null
                                                                ? sleepAnalysisService.getSleepEfficiencyRating(
                                                                                tracker.getSleepEfficiency())
                                                                : null)
                                .complianceRate(tracker.getComplianceRate())
                                .complianceRating(
                                                tracker.getComplianceRate() != null
                                                                ? sleepAnalysisService.getComplianceRating(
                                                                                tracker.getComplianceRate())
                                                                : null)
                                .notes(tracker.getNotes())
                                .date(tracker.getDate())
                                .status(tracker.getStatus())
                                .build();
        }

        // ========== Quick Log Methods Implementation ==========

        @Override
        public SleepTrackerResponse logBedtime(int userId, LocalDateTime bedtime) {
                // 1. Check for existing PENDING record
                SleepTracker pending = sleepTrackerRepository.findByUserIdAndStatus((long) userId, SleepStatus.PENDING);
                if (pending != null) {
                        throw new IllegalStateException(
                                        "Bạn đã có giấc ngủ đang chờ hoàn thành. Vui lòng hoàn thành trước khi tạo mới.");
                }

                // 2. Calculate date intelligently
                // If bedtime is before 6 AM (00:00-05:59), it belongs to the same day
                // If bedtime is 6 AM or later, it belongs to the next day
                LocalDate sleepDate;
                if (bedtime.getHour() < 6) {
                        sleepDate = bedtime.toLocalDate();
                } else {
                        sleepDate = bedtime.toLocalDate().plusDays(1);
                }

                // 3. Check duplicate for that date
                boolean exists = sleepTrackerRepository.existsByUserIdAndDate((long) userId, sleepDate);
                if (exists) {
                        throw new IllegalStateException(
                                        "Ngày " + sleepDate + " đã có giấc ngủ. Mỗi ngày chỉ 1 giấc ngủ.");
                }

                // 4. Get user
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                // 5. Create PENDING tracker
                SleepTracker tracker = new SleepTracker();
                tracker.setUser(user);
                tracker.setDate(sleepDate.atStartOfDay()); // Convert LocalDate → LocalDateTime
                tracker.setSleepTime(bedtime);
                tracker.setStatus(SleepStatus.PENDING);
                // wakeTime, duration, quality = null for PENDING

                // 6. Save
                SleepTracker saved = sleepTrackerRepository.save(tracker);

                return mapToResponse(saved);
        }

        @Override
        public SleepTrackerResponse completeSleep(int userId, LocalDateTime wakeTime, Integer quality) {
                // 1. Find PENDING record
                SleepTracker tracker = sleepTrackerRepository.findByUserIdAndStatus(
                                (long) userId, SleepStatus.PENDING);
                if (tracker == null) {
                        throw new ResourceNotFoundException(
                                        "Không tìm thấy giấc ngủ đang chờ hoàn thành. Vui lòng ghi giờ đi ngủ trước.");
                }

                // 2. Validate wake time
                if (wakeTime.isBefore(tracker.getSleepTime()) || wakeTime.isEqual(tracker.getSleepTime())) {
                        throw new IllegalArgumentException("Giờ thức dậy phải sau giờ đi ngủ");
                }

                long minutes = Duration.between(tracker.getSleepTime(), wakeTime).toMinutes();
                if (minutes < 60) {
                        throw new IllegalArgumentException("Giấc ngủ quá ngắn (< 1 giờ)");
                }
                if (minutes > 960) {
                        throw new IllegalArgumentException("Giấc ngủ quá dài (> 16 giờ)");
                }

                // 3. Update tracker with accurate date based on wakeTime
                tracker.setDate(wakeTime.toLocalDate().atStartOfDay()); // Accurate date = wake date
                tracker.setWakeTime(wakeTime);
                tracker.setQuality(quality != null ? quality : 3); // Default quality = 3
                tracker.setStatus(SleepStatus.COMPLETED);

                // 4. Calculate metrics
                calculateMetrics(tracker, userId);

                // 5. Save
                SleepTracker updated = sleepTrackerRepository.save(tracker);

                return mapToResponse(updated);
        }

        @Override
        public TodaySleepResponse getTodaySleep(int userId, LocalDate date) {
                // Find by exact date
                SleepTracker tracker = sleepTrackerRepository
                                .findByUserIdAndDateEquals((long) userId, date);

                if (tracker == null) {
                        // No record
                        return TodaySleepResponse.builder()
                                        .hasRecord(false)
                                        .date(date)
                                        .build();
                }

                // Has record
                return TodaySleepResponse.builder()
                                .hasRecord(true)
                                .date(date)
                                .status(tracker.getStatus())
                                .data(mapToResponse(tracker))
                                .build();
        }
}
