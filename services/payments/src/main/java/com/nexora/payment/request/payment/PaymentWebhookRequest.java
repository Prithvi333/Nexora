package com.nexora.payment.request.payment;

public record PaymentWebhookRequest(

        String gatewayPaymentId,

        String paymentId,

        String status,

        String signature,
        String rawPayload
) {
}
