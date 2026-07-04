package com.nexora.product.response.product;

import com.nexora.product.response.category.CategoryResponse;
import com.nexora.product.response.variant.ProductVariantResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProductResponse(

        String uid,
        String name,
        String description,
        String brand,
        boolean active,
        LocalDateTime createdAt,

        CategoryResponse category,

        List<ProductVariantResponse> productVariants
) {
}