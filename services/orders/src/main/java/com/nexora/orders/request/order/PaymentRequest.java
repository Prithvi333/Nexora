package com.nexora.orders.request.order;

import jakarta.validation.constraints.NotBlank;

public record PaymentRequest(
        @NotBlank(message = "Current is required")
        String currency,
        @NotBlank(message = "Payment method is required")
        String paymentMethod,
        @NotBlank(message = "order id is required")
        String orderUid

) {
}
