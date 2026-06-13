package com.nexora.common.events;

import com.nexora.auth.kafka.enums.EventType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class BaseEvent {
    String userUid;
    EventType eventType;
    String username;
    String email;
}
