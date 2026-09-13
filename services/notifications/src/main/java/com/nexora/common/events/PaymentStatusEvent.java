package com.nexora.common.events;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@SuperBuilder
@Getter
@Jacksonized
public class PaymentStatusEvent extends BaseEvent {
    @NotBlank(message = "order uid is required")
    String orderUid;
    @NotBlank(message = "Email is required")
    String email;
    String orderStatus;
    String paymentStatus;
}
