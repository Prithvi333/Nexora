package com.nexora.product.request.inventory;

import jakarta.validation.constraints.NotBlank;

public record InventoryUpdateRequest(

        @NotBlank
        String uid,

        Integer quantity,

        Integer reserved
) {
}
