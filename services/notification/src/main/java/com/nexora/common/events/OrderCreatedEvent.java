package com.nexora.common.events;

import com.nexora.notification.kafka.enums.EventType;


public record OrderCreatedEvent(

        String userUid,

        String email,

        String orderUid,

        Double amount,

        EventType eventType
) {
}
