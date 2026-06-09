package com.nexora.payment.response.payment;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentResponse(

        String paymentId,

        String orderUid,

        String userUid,

        Double amount,

        String currency,

        String status,

        String paymentMethod,

        String gatewayName,

        String gatewayPaymentId,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {
}