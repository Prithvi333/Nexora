package com.nexora.common.events;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Getter
@Jacksonized
public class PaymentRequestEvent extends BaseEvent {
    String currency;
    Double amount;
    String paymentMethod;
    String orderUid;
}
