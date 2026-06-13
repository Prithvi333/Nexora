package com.nexora.orders.response.orderItems;

import lombok.Builder;

@Builder
public record OrderItemResponse(
        String uid,
        String productUid,
        String variantUid,
        Integer quantity,
        Double price
) {
}
