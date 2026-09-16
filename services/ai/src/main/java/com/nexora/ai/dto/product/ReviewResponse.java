package com.nexora.ai.dto.product;

import java.time.LocalDateTime;

public record ReviewResponse(
        String uid,
        String productUid,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}