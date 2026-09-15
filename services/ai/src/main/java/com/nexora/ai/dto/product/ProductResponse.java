package com.nexora.ai.dto.product;

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