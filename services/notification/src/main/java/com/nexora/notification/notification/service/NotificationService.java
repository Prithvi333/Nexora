package com.nexora.notification.notification.service;

import com.nexora.notification.notification.enums.NotificationEventType;
import com.nexora.notification.response.notification.NotificationResponse;

import java.util.List;

public interface NotificationService {

    void sendNotification(String to, String body, String userUid, NotificationEventType eventType);

    List<NotificationResponse> getAllNotification(Integer pageNo, Integer pageSize, String sortBy, String direction);

}
