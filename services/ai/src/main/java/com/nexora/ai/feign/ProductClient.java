package com.nexora.ai.feign;

import com.nexora.ai.dto.product.ProductResponse;
import com.nexora.ai.dto.product.ReviewResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "products", url = "localhost:8086")
public interface ProductClient {

    @GetMapping("/internal/product/products")
    ResponseEntity<List<ProductResponse>> getAllProducts();

    @PostMapping("/api/products/user/product")
    ResponseEntity<List<ProductResponse>> fetchProductByProductUidList(@RequestBody List<String> productUids);


    @GetMapping("/api/products/user/review/product/{productUid}")
    ResponseEntity<List<ReviewResponse>> getReviewsByProduct(
            @PathVariable String productUid);

    @GetMapping("api/products/user/product/{productUid}")
    ResponseEntity<ProductResponse> findProductByUid(@PathVariable String productUid);

}