package com.nexora.notification.response.notification;

import com.nexora.notification.kafka.enums.EventType;
import com.nexora.notification.notification.enums.NotificationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationResponse(
        String userId,

        String email,

        String subject,

        String message,

        NotificationStatus status,

        EventType eventType,

        LocalDateTime createdAt
) {
}
