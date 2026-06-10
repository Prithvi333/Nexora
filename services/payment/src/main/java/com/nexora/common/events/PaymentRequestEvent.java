package com.nexora.common.events;

import lombok.Builder;

@Builder
public record PaymentRequestEvent(
        String currency,
        String userUid,
        String paymentMethod,
        String orderUid
) {
}