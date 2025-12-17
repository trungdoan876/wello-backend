package com.wello.wellobackend.enums;

public enum AuthProvider {
    EMAIL("Email/Password"),
    GOOGLE("Google Sign-In");

    private final String displayName;

    AuthProvider(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
