package com.nexora.orders.response.order;

import com.nexora.orders.order.enums.OrderStatus;
import com.nexora.orders.response.orderItems.OrderItemResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        String orderUid,
        String userUid,
        OrderStatus status,
        BigDecimal totalAmount,
        List<OrderItemResponse> items,
        LocalDateTime createdAt
) {
}
