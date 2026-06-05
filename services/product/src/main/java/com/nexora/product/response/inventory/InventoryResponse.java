package com.nexora.product.response.inventory;

import lombok.Builder;

@Builder
public record InventoryResponse(
        String uid,
        Integer quantity,
        Integer reserved
) {
}
