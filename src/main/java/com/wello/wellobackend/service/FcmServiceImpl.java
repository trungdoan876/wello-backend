package com.wello.wellobackend.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

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

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent FCM message: " + response);
        } catch (Exception e) {
            System.err.println("Error sending FCM message: " + e.getMessage());
        }
    }
}
