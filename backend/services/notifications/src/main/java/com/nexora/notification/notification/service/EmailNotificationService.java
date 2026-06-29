package com.nexora.notification.notification.service;

public interface EmailNotificationService {
    void sendEmail(String to, String subject, String body);
}
