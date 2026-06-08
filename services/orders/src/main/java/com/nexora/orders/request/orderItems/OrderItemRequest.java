package com.nexora.orders.request.orderItems;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull(message = "Product uid is required")
        String productUid,

        @NotNull(message = "Variant uid is required")
        String variantUid,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}
