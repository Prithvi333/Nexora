package com.nexora.product.response.image;

import lombok.Builder;

@Builder
public record ProductImageResponse(

        String uid,
        String url,
        boolean primary
) {
}
