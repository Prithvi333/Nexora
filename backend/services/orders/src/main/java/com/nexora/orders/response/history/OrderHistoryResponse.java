package com.nexora.orders.response.history;

import com.nexora.orders.order.enums.OrderStatus;
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
