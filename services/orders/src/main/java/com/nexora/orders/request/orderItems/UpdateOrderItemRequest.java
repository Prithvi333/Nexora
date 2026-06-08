package com.nexora.orders.request.orderItems;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateOrderItemRequest(
        @NotNull(message = "OrderId cannot be null")
        String orderUid,

        @NotEmpty(message = "Order must contain at least one item")

        @Valid
        List<UpdateOrderItemRequest> items
) {
}
