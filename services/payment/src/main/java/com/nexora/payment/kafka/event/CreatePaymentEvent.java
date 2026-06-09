package com.nexora.payment.kafka.event;

import com.nexora.payment.payment.enums.CurrencyType;
import com.nexora.payment.payment.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentEvent(

        @NotNull(message = "Order uid is required")
        String orderUid,

        @NotNull(message = "User uid is required")
        String userUid,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "1.0", message = "Amount must be greater than 0")
        Double amount,

        @NotNull(message = "Currency should not be null")
        CurrencyType currencyType,

        @NotNull(message = "Payment method should not be null")
        PaymentMethod paymentMethod,

        @NotBlank(message = "Idempotency key required")
        String idempotencyKey
) {
}
