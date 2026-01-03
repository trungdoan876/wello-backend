package com.wello.wellobackend.service;

import com.wello.wellobackend.model.SleepTracker;
import com.wello.wellobackend.repository.SleepTrackerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service phân tích giấc ngủ
 * Tính toán các metrics: efficiency, compliance rate, trends, insights
 */
@Service
@RequiredArgsConstructor
public class SleepAnalysisService {

    private final SleepTrackerRepository sleepTrackerRepository;

    /**
     * Tính Sleep Efficiency (hiệu suất giấc ngủ)
     * Formula: (Total Sleep Time / Time in Bed) × 100%
     * 
     * @param tracker SleepTracker
     * @return efficiency percentage
     */
    public double calculateSleepEfficiency(SleepTracker tracker) {
        if (tracker.getSleepTime() == null || tracker.getWakeTime() == null) {
            return 0.0;
        }

        long totalMinutes = Duration.between(
                tracker.getSleepTime(),
                tracker.getWakeTime()).toMinutes();

        if (totalMinutes == 0) {
            return 0.0;
        }

        double efficiency = (tracker.getDuration() / (double) totalMinutes) * 100.0;

        // Làm tròn 2 chữ số
        return Math.round(efficiency * 100.0) / 100.0;
    }

    /**
     * Lấy rating cho sleep efficiency
     */
    public String getSleepEfficiencyRating(double efficiency) {
        if (efficiency >= 85)
            return "Excellent";
        if (efficiency >= 75)
            return "Good";
        return "Poor";
    }

    /**
     * Tính Compliance Rate (tỷ lệ tuân thủ)
     * Formula: (Actual Hours / Recommended Hours) × 100%
     */
    public double calculateComplianceRate(double actualHours, double recommendedHours) {
        if (recommendedHours == 0) {
            return 0.0;
        }

        double rate = (actualHours / recommendedHours) * 100.0;
        return Math.round(rate * 100.0) / 100.0;
    }

    /**
     * Lấy rating cho compliance rate
     */
    public String getComplianceRating(double complianceRate) {
        if (complianceRate >= 90 && complianceRate <= 110) {
            return "Optimal"; // 90-110%: Lý tưởng
        }
        if ((complianceRate >= 80 && complianceRate < 90) ||
                (complianceRate > 110 && complianceRate <= 120)) {
            return "Acceptable"; // 80-89% hoặc 111-120%
        }
        return "Poor"; // < 80% hoặc > 120%
    }

    /**
     * Tính Sleep Debt (nợ giấc ngủ)
     * 
     * @param userId      user ID
     * @param days        số ngày để tính (thường là 7)
     * @param targetHours số giờ ngủ mục tiêu
     * @return tổng giờ thiếu/thừa (dương = thiếu, âm = thừa)
     */
    public double calculateSleepDebt(Long userId, int days, double targetHours) {
        // Get logs từ N ngày gần nhất
        java.time.LocalDateTime endDate = java.time.LocalDateTime.now();
        java.time.LocalDateTime startDate = endDate.minusDays(days - 1);

        List<SleepTracker> trackers = sleepTrackerRepository
                .findByUserIdAndDateBetween(userId, startDate, endDate);

        double totalDebt = 0.0;
        for (SleepTracker tracker : trackers) {
            double actualHours = tracker.getDuration() / 60.0;
            double deficit = targetHours - actualHours;
            totalDebt += deficit;
        }

        return Math.round(totalDebt * 10.0) / 10.0; // Làm tròn 1 chữ số
    }

    /**
     * Phân tích xu hướng giấc ngủ
     * 
     * @return "improving", "stable", "declining"
     */
    public String analyzeTrends(List<SleepTracker> trackers) {
        if (trackers.size() < 4) {
            return "stable"; // Không đủ dữ liệu
        }

        // Chia làm 2 nửa
        int mid = trackers.size() / 2;
        List<SleepTracker> firstHalf = trackers.subList(0, mid);
        List<SleepTracker> secondHalf = trackers.subList(mid, trackers.size());

        double firstAvg = firstHalf.stream()
                .mapToDouble(SleepTracker::getDuration)
                .average()
                .orElse(0.0);

        double secondAvg = secondHalf.stream()
                .mapToDouble(SleepTracker::getDuration)
                .average()
                .orElse(0.0);

        double diff = secondAvg - firstAvg; // phút

        if (diff > 15)
            return "improving"; // +15 phút
        if (diff < -15)
            return "declining"; // -15 phút
        return "stable";
    }

    /**
     * Tạo insights thông minh
     */
    public List<String> generateInsights(List<SleepTracker> trackers, int age,
            double targetHours, double sleepDebt) {
        List<String> insights = new ArrayList<>();

        if (trackers.isEmpty()) {
            insights.add("Chưa có dữ liệu giấc ngủ. Hãy bắt đầu ghi nhận!");
            return insights;
        }

        int totalDays = 7; // hoặc tính từ period
        int trackedDays = trackers.size();
        double trackingRate = (trackedDays / (double) totalDays) * 100;

        // Insight về tracking completion
        if (trackedDays == totalDays) {
            insights.add("✅ Tuyệt vời! Bạn đã ghi nhận đều đặn " + totalDays + " đêm");
        } else {
            insights.add(String.format(
                    "✅ Bạn đã ghi nhận %d/%d đêm tuần này (%.0f%%)",
                    trackedDays, totalDays, trackingRate));
        }

        // Phân tích theo ngày trong tuần
        if (trackers.size() >= 3) {
            Map<DayOfWeek, Double> avgByDay = trackers.stream()
                    .collect(Collectors.groupingBy(
                            t -> t.getDate().toLocalDate().getDayOfWeek(),
                            Collectors.averagingDouble(t -> t.getDuration() / 60.0)));

            if (!avgByDay.isEmpty()) {
                DayOfWeek bestDay = Collections.max(avgByDay.entrySet(),
                        Map.Entry.comparingByValue()).getKey();
                insights.add("🏆 Bạn ngủ tốt nhất vào " + getDayNameInVietnamese(bestDay));
            }
        }

        // Sleep debt warning
        if (sleepDebt > 3) {
            insights.add(String.format(
                    "😴 Bạn đang thiếu %.1f giờ ngủ. Hãy ngủ bù vào cuối tuần!",
                    sleepDebt));
        } else if (sleepDebt < -3) {
            insights.add(String.format(
                    "💤 Bạn đang ngủ thừa %.1f giờ. Có thể điều chỉnh lại mục tiêu?",
                    Math.abs(sleepDebt)));
        }

        // Pattern: ngủ muộn
        long lateNights = trackers.stream()
                .filter(t -> t.getSleepTime().toLocalTime().isAfter(java.time.LocalTime.of(23, 30)))
                .count();

        if (lateNights > trackers.size() * 0.7) {
            insights.add("⚠️ Bạn thường xuyên đi ngủ muộn. Thử đi ngủ sớm hơn 30 phút?");
        }

        return insights;
    }

    /**
     * Helper: convert DayOfWeek sang tiếng Việt
     */
    private String getDayNameInVietnamese(DayOfWeek day) {
        return switch (day) {
            case MONDAY -> "Thứ Hai";
            case TUESDAY -> "Thứ Ba";
            case WEDNESDAY -> "Thứ Tư";
            case THURSDAY -> "Thứ Năm";
            case FRIDAY -> "Thứ Sáu";
            case SATURDAY -> "Thứ Bảy";
            case SUNDAY -> "Chủ Nhật";
        };
    }
}
