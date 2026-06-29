package com.nexora.product.request.inventory;

import jakarta.validation.constraints.NotNull;

public record InventoryRequest(
        @NotNull
        Integer quantity,

        @NotNull
        Integer reserved
) {
}
