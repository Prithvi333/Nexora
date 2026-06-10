package com.nexora.payment.request.payment;

import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
public record CreatePaymentRequest(

        @NotBlank(message = "User UID must not be blank")
        String userUid,

        @NotBlank(message = "Order UID must not be blank")
        String orderUid,

        @NotNull(message = "Amount must not be null")
        @Positive(message = "Amount must be greater than 0")
        @Digits(integer = 10, fraction = 2, message = "Amount must have at most 2 decimal places")
        Double amount,

        @NotBlank(message = "Payment method must not be blank")
        @Pattern(regexp = "^(UPI|CARD|NET_BANKING|WALLET|COD)$", message = "Invalid payment method")
        String paymentMethod,

        @NotBlank(message = "Currency must not be blank")
        @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be uppercase ISO 4217 code e.g. INR")
        String currency
) {
}
