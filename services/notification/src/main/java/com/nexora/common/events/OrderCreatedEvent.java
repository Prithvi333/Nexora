package com.nexora.common.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Jacksonized
@Getter
public class OrderCreatedEvent extends BaseEvent {
    String orderUid;
    Double amount;
}
