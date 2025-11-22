package com.wello.wellobackend.enums;

public enum Goal {
    GAIN_WEIGHT("Tăng cân"),
    LOSE_WEIGHT("Giảm cân"),
    KEEP_FIT("Giữ dáng");

    private final String moTa;

    Goal(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
