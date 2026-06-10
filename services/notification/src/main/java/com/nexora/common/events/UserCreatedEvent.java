package com.nexora.common.events;

import com.nexora.notification.kafka.enums.EventType;

public record UserCreatedEvent(
        String userUid,
        String username,
        String email,
        EventType eventType
) {
}