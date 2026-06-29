package com.nexora.orders.response.order;

import com.nexora.orders.order.enums.OrderStatus;
import com.nexora.orders.response.orderItems.OrderItemResponse;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponse(
        String orderUid,
        String userProfileUId,
        OrderStatus status,
        Double totalAmount,
        List<OrderItemResponse> items,
        LocalDateTime createdAt
) {
}
