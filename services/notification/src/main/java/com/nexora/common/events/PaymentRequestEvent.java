package com.nexora.common.events;

public record PaymentRequestEvent(
        String currency,
        String userUid,
        String paymentMethod,
        String orderUid
) {
}
