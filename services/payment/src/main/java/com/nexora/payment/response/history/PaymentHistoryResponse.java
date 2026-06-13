package com.nexora.payment.response.history;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PaymentHistoryResponse(

        String paymentUid,

        String fromStatus,

        String toStatus,

        String eventType,

        String triggeredBy,

        String description,

        LocalDateTime createdAt

) {
}
