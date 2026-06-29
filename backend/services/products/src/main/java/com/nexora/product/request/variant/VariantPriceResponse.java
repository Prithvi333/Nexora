package com.nexora.product.request.variant;

import lombok.Builder;

@Builder
public record VariantPriceResponse(
        String variantUid,
        Double price
) {
}