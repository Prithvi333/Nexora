package com.nexora.ai.dto.product;

import lombok.Builder;

@Builder
public record InventoryResponse(
        String uid,
        Integer quantity,
        Integer reserved
) {
}
