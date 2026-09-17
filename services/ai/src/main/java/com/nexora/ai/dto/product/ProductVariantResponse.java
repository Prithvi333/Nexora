package com.nexora.ai.dto.product;


import lombok.Builder;

import java.util.List;

@Builder
public record ProductVariantResponse(

        String uid,
        String size,
        String color,
        Double price,

        InventoryResponse inventory,

        List<ProductImageResponse> productImages
) {
}