package com.nexora.common.events;

import com.nexora.auth.kafka.enums.EventType;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class UserCreatedEvent extends BaseEvent {

}
