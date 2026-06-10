package com.nexora.common.events;

import com.nexora.orders.kafka.enums.EventType;
import lombok.Builder;

@Builder
public record OrderCreatedEvent(
        String userUid,

        String email,

        String orderUid,

        Double amount,

        EventType eventType
) {
}
