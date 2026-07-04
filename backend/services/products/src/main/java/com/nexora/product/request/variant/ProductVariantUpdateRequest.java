package com.nexora.product.request.variant;

import jakarta.validation.constraints.Min;

public record ProductVariantUpdateRequest(

        String uid,
        String color,
        String size,

        @Min(0)
        Double price,

        String productUid
) {
}
