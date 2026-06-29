package com.nexora.orders.request.order;

import com.nexora.orders.request.orderItems.OrderItemRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(

        @NotNull(message = "User profile uid cannot be null")
        String userProfileUid,

        @NotEmpty(message = "Order must contain at least one item")
        List<OrderItemRequest> items
) {
}
