package com.nexora.orders.request.orderItems;

import jakarta.validation.constraints.NotNull;


public record UpdateOrderItemRequest(

        @NotNull(message = "user profile uid is required")
        String userProfileUid,
        @NotNull(message = "Order item uid cannot be null")
        String orderItemUid,

        @NotNull(message = "Order quantity can not be null")
        Integer quantity,

        @NotNull
        Boolean increment
) {
}
