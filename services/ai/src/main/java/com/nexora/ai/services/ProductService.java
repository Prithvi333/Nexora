package com.nexora.ai.services;

import com.nexora.ai.dto.product.ProductResponse;
import com.nexora.ai.dto.product.ReviewSummaryResponse;

import java.util.List;

public interface ProductService {

    String generateProductDescription(String productName);

    List<ProductResponse> productSmartSearch(String productSearchQuery);

    List<ProductResponse> productRecommendation();

    ReviewSummaryResponse summarizeReviews(String productUid);

    String chat(String prompt);

}
