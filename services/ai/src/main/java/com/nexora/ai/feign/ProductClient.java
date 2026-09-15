package com.nexora.ai.feign;

import com.nexora.ai.dto.product.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "products", url = "localhost:8086")
public interface ProductClient {

    @GetMapping("/api/products/user/product/products")
    ResponseEntity<List<ProductResponse>> getAllProducts();

    @PostMapping("/api/products/user/product")
    ResponseEntity<List<ProductResponse>> fetchProductByProductUidList(@RequestBody List<String> productUids);

}