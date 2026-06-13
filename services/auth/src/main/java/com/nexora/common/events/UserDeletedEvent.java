package com.nexora.common.events;

import com.nexora.auth.kafka.enums.EventType;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class UserDeletedEvent extends BaseEvent {

}
