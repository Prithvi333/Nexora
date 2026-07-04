package com.nexora.common.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Getter
@Jacksonized
public class OrderCreatedEvent extends BaseEvent {
    String email;
    String orderUid;
    Double amount;
}
