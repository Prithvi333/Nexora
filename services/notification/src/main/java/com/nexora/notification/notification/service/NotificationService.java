package com.nexora.notification.notification.service;

import com.nexora.notification.request.notification.NotificationRequest;
import com.nexora.notification.response.notification.NotificationResponse;

import java.util.List;

public interface NotificationService {

    void sendNotification(NotificationRequest notificationRequest);

    List<NotificationResponse> getAllNotification(Integer pageNo, Integer pageSize, String sortBy, String direction);

}
