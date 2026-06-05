package com.nexora.product.response.variant;

import com.nexora.product.response.image.ProductImageResponse;
import com.nexora.product.response.inventory.InventoryResponse;
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
