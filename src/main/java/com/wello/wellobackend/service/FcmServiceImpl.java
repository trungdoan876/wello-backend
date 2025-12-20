package com.wello.wellobackend.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FcmServiceImpl implements FcmService {

    @Override
    public void sendPushNotification(String token, String title, String body) {
        if (token == null || token.isEmpty()) {
            return;
        }

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        // Add data payload for Flutter to identify notification type
        Map<String, String> data = new HashMap<>();
        data.put("type", "water_reminder");
        data.put("title", title);
        data.put("body", body);

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .putAllData(data)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent FCM message: " + response);
        } catch (Exception e) {
            System.err.println("Error sending FCM message: " + e.getMessage());
        }
    }
}
