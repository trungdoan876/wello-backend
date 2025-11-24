package com.wello.wellobackend.enums;

public enum Gender {
    MALE("Nam"),
    FEMALE("Nữ");

    private final String moTa;

    Gender(String moTa) {
        this.moTa = moTa;
    }

    public String getMoTa() {
        return moTa;
    }
}
