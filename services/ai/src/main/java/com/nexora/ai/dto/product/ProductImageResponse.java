package com.nexora.ai.dto.product;

import lombok.Builder;

@Builder
public record ProductImageResponse(

        String uid,
        String url,
        boolean primary
) {
}
