package com.nexora.common.events;

import com.nexora.user.kafka.enums.EventType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class BaseEvent {
    String userUid;
    String username;
    String email;
    EventType eventType;
}
