package com.nexora.product.request.variant;

import com.nexora.product.request.inventory.InventoryRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductVariantRequest(
        @NotBlank
        String color,

        @NotBlank
        String size,

        @Min(0)
        Double price,

        @NotNull
        String productUid,
        @NotNull(message = "Inventory is required for product variant")
        @Valid
        InventoryRequest inventory
) {
}
