package com.nexora.common.events;

import com.nexora.auth.kafka.enums.EventType;
import lombok.Builder;

@Builder
public record UserCreatedEvent(
        String userUid,
        String username,
        String email,
        EventType eventType
) {
}
