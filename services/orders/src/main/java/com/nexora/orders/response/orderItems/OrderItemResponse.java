package com.nexora.orders.response.orderItems;

public record OrderItemResponse(
        String productUid,
        Integer quantity,
        Double price
) {
}
