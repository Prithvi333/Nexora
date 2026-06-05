package com.nexora.product.product.controller;

import com.nexora.product.product.service.ProductService;
import com.nexora.product.request.product.ProductRequest;
import com.nexora.product.request.product.ProductUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.product.ProductResponse;
import com.nexora.product.utility.constants.IUrls;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(IUrls.ADMIN + IUrls.PRODUCT)
public class ProductController {
    @Autowired
    private ProductService productService;


    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateProduct(@Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        return new ResponseEntity<>(productService.updateProduct(productUpdateRequest), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteProduct(@RequestParam("productUid") String productUid) {
        return new ResponseEntity<>(productService.deleteProduct(productUid), HttpStatus.NO_CONTENT);
    }
}
