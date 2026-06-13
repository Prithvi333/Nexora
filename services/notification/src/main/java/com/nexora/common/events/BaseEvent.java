package com.nexora.common.events;

import com.nexora.notification.kafka.enums.EventType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Getter
@Jacksonized
public class BaseEvent {
    String userUid;
    String username;
    String email;
    EventType eventType;

}
