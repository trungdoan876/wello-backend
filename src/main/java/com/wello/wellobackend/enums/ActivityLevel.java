package com.wello.wellobackend.enums;

public enum ActivityLevel {
    SEDENTARY(
            "Chỉ làm việc văn phòng, ngồi nhiều, không tập thể dục.",
            1.2
    ),

    LIGHT_ACTIVE(
            "Tập thể dục nhẹ nhàng 1-3 ngày/tuần (ví dụ: đi bộ, yoga nhẹ).",
            1.375
    ),

    MODERATE_ACTIVE(
            "Tập thể dục với cường độ vừa phải 3-5 ngày/tuần (ví dụ: chạy bộ, gym, bơi lội).",
            1.55
    ),

    HEAVY_ACTIVE(
            "Tập luyện cường độ cao 6-7 ngày/tuần.",
            1.725
    ),

    VERY_HEAVY_ACTIVE(
            "Vận động viên chuyên nghiệp, tập 2 lần/ngày, hoặc công việc lao động chân tay rất nặng",
            1.9
    );

    private final String moTa;
    private final double heSoTDEE;

    ActivityLevel(String moTa, double heSoTDEE) {
        this.moTa = moTa;
        this.heSoTDEE = heSoTDEE;
    }

    public String getMoTa() {
        return moTa;
    }

    public double getHeSoTDEE() {
        return heSoTDEE;
    }
}
