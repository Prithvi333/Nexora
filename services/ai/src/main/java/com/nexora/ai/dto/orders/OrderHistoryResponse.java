package com.nexora.ai.dto.orders;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderHistoryResponse(
        String historyUid,
        String orderUid,
        OrderStatus fromStatus,
        OrderStatus toStatus,
        String actionBy,
        String reason,
        LocalDateTime timestamp
) {
}