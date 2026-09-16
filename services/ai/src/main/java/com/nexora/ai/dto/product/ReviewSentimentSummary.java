package com.nexora.ai.dto.product;

public record ReviewSentimentSummary(
        Integer positiveReviews,
        Integer negativeReviews,
        Integer neutralReviews
) {
}
