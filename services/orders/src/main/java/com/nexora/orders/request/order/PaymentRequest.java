package com.nexora.orders.request.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotBlank(message = "Current is required")
        String currency,

        @NotBlank(message = "user uid should not be blank")
        String userUid,
        @NotBlank(message = "Payment method is required")
        String paymentMethod,
        @NotBlank(message = "order id is required")
        String orderUid

) {
}
