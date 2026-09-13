package com.nexora.product.product.controller;
import com.nexora.product.product.service.ProductService;
import com.nexora.product.request.product.ProductRequest;
import com.nexora.product.request.product.ProductUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.product.ProductResponse;
import com.nexora.product.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping(IUrls.ADMIN + IUrls.PRODUCT)
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;


    @PostMapping
    @Operation(summary = "Create product", description = "Used to create the product")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest) {
        logger.info("Received request to create product");
        ResponseEntity<ProductResponse> response = new ResponseEntity<>(productService.createProduct(productRequest), HttpStatus.CREATED);
        logger.info("Product created successfully");
        return response;
    }

    @GetMapping
    @Operation(summary = "Fetch product", description = "Used to fetch the product")
    public ResponseEntity<List<ProductResponse>> fetchProduct(@RequestParam(required = false) String productUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        logger.info("Received request to fetch product with productUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", productUid, pageNo, pageSize, sortBy, direction);
        ResponseEntity<List<ProductResponse>> response = new ResponseEntity<>(productService.fetchProduct(productUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("Product fetched successfully");
        return response;
    }

    @PutMapping
    @Operation(summary = "Update product", description = "Used to update the product")
    public ResponseEntity<SuccessResponse> updateProduct(@Valid @RequestBody ProductUpdateRequest productUpdateRequest) {
        logger.info("Received request to update product");
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(productService.updateProduct(productUpdateRequest), HttpStatus.OK);
        logger.info("Product updated successfully");
        return response;
    }

    @DeleteMapping
    @Operation(summary = "Delete product", description = "Used to delete the product")
    public ResponseEntity<SuccessResponse> deleteProduct(@RequestParam("productUid") String productUid) {
        logger.info("Received request to delete product with productUid: {}", productUid);
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(productService.deleteProduct(productUid), HttpStatus.NO_CONTENT);
        logger.info("Product deleted successfully with productUid: {}", productUid);
        return response;
    }

}