package com.nexora.ai.dto.product;

import java.util.List;

public record ReviewSummaryResponse(
        Integer totalReviews,
        Double averageRating,
        String overallSentiment,
        String summary,
        ReviewSentimentSummary sentiment,
        List<String> strengths,
        List<String> weaknesses,
        List<String> improvementAreas
) {
}
