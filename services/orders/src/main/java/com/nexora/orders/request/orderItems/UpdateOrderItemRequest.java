package com.nexora.orders.request.orderItems;

import jakarta.validation.constraints.NotNull;


public record UpdateOrderItemRequest(
        @NotNull(message = "Order item uid cannot be null")
        String orderItemUid,

        @NotNull(message = "Order quantity can not be null")
        Integer quantity
) {
}
