package com.wello.wellobackend.enums;

/**
 * Enum for sleep tracking status
 * PENDING: User has logged bedtime but not wake time yet
 * COMPLETED: User has logged both bedtime and wake time
 */
public enum SleepStatus {
    PENDING, // Đang ngủ - chưa ghi wake time
    COMPLETED // Đã hoàn thành - có đủ bedtime và wake time
}
