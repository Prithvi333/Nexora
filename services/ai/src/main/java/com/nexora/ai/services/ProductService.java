package com.nexora.ai.services;

import com.nexora.ai.dto.product.ProductResponse;

import java.util.List;

public interface ProductService {

    String generateProductDescription(String productName);

    List<ProductResponse> productSmartSearch(String productSearchQuery);

    List<ProductResponse> productRecommendation();

    String chat(String prompt);

}
