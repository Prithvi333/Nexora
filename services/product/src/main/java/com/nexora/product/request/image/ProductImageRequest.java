package com.nexora.product.request.image;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductImageRequest(

        @NotBlank
        String url,

        Boolean primary,

        @NotNull(message = "product variant uid is required")
        String productVariantUid

) {
}
