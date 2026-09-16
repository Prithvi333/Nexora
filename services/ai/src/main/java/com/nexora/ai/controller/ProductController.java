package com.nexora.ai.controller;

import com.nexora.ai.dto.product.ProductResponse;
import com.nexora.ai.dto.product.ReviewSummaryResponse;
import com.nexora.ai.services.ProductService;
import com.nexora.ai.utility.IUrls;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(IUrls.USER + IUrls.PRODUCTS)
public class ProductController {

    private final ProductService productService;

    @GetMapping("/description")
    public ResponseEntity<String> generateProductDescription(@RequestParam String productName) {
        log.info("REST request to generate product description for productName: {}", productName);
        String result = productService.generateProductDescription(productName);
        log.info("Successfully generated product description for productName: {}", productName);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> userProductSmartSearch(@RequestParam String searchQuery) {
        log.info("REST request for smart search with query: '{}'", searchQuery);
        List<ProductResponse> response = productService.productSmartSearch(searchQuery);
        log.info("Smart search returned {} products for query: '{}'", response.size(), searchQuery);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<List<ProductResponse>> userProductRecommendation() {
        log.info("REST request to generate user product recommendations");
        List<ProductResponse> recommendations = productService.productRecommendation();
        log.info("Successfully fetched {} product recommendations", recommendations.size());
        return ResponseEntity.ok(recommendations);
    }

    @PutMapping
    public ResponseEntity<String> chatting(@RequestParam String prompt) {
        log.info("REST request for chat with prompt length: {} chars", prompt != null ? prompt.length() : 0);
        String response = productService.chat(prompt);
        log.info("Successfully processed chat request");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/review/report")
    public ResponseEntity<ReviewSummaryResponse> summarizeReview(@RequestParam String productUid) {
        log.info("REST request to summarize reviews for productUid: {}", productUid);
        ReviewSummaryResponse summary = productService.summarizeReviews(productUid);
        log.info("Successfully generated review summary for productUid: {}", productUid);
        return ResponseEntity.ok(summary);
    }
}