package com.wello.wellobackend.service;

public interface FcmService {
    void sendPushNotification(String token, String title, String body);
}
