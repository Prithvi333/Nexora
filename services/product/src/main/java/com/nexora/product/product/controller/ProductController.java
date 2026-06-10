package com.nexora.product.product.controller;

import com.nexora.product.product.service.ProductService;
import com.nexora.product.request.product.ProductRequest;
import com.nexora.product.request.product.ProductUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.product.ProductResponse;
import com.nexora.product.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(IUrls.ADMIN + IUrls.PRODUCT)
public class ProductController {
    @Autowired
    private ProductService productService;


    @PostMapping
    @Operation(summary = "Create product", description = "Used to create the product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        return new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Fetch product", description = "Used to fetch the product")
    public ResponseEntity<List<ProductResponse>> fetchProduct(@RequestParam(required = false) String productUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(productService.fetchProduct(productUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

    @PutMapping
    @Operation(summary = "Update product", description = "Used to update the product")
    public ResponseEntity<SuccessResponse> updateProduct(@Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        return new ResponseEntity<>(productService.updateProduct(productUpdateRequest), HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "Delete product", description = "Used to delete the product")
    public ResponseEntity<SuccessResponse> deleteProduct(@RequestParam("productUid") String productUid) {
        return new ResponseEntity<>(productService.deleteProduct(productUid), HttpStatus.NO_CONTENT);
    }
}
