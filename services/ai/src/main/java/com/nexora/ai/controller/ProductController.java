package com.nexora.ai.controller;

import com.nexora.ai.dto.product.ProductResponse;
import com.nexora.ai.services.ProductService;
import com.nexora.ai.utility.IUrls;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(IUrls.USER + IUrls.PRODUCTS)
public class ProductController {

    private final ProductService productService;

    @GetMapping("/description")
    public ResponseEntity<String> generateProductDescription(@RequestParam String productName) {
        return ResponseEntity.ok(productService.generateProductDescription(productName));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> userProductSmartSearch(@RequestParam String searchQuery) {
        return ResponseEntity.ok(productService.productSmartSearch(searchQuery));
    }

    @PostMapping
    public ResponseEntity<List<ProductResponse>> userProductRecommendation() {
        return ResponseEntity.ok(productService.productRecommendation());
    }

    @PutMapping
    public ResponseEntity<String> chatting(@RequestParam String prompt) {
        return ResponseEntity.ok(productService.chat(prompt));
    }

}
