package com.nexora.product.variant.controller;
import com.nexora.product.request.variant.ProductVariantRequest;
import com.nexora.product.request.variant.ProductVariantUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.variant.ProductVariantResponse;
import com.nexora.product.utility.constants.IUrls;
import com.nexora.product.variant.service.ProductVariantService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(IUrls.ADMIN + IUrls.VARIANT)
public class ProductVariantController {
    private static final Logger logger = LoggerFactory.getLogger(ProductVariantController.class);

    @Autowired
    private ProductVariantService productVariantService;

    @PostMapping
    @Operation(summary = "Create product  variant", description = "Used to create the product variant")
    public ResponseEntity<ProductVariantResponse> createProductVariant(@Valid @RequestBody ProductVariantRequest productVariantRequest) {
        logger.info("Received request to create product variant");
        ResponseEntity<ProductVariantResponse> response = new ResponseEntity<>(productVariantService.createProductVariant(productVariantRequest), HttpStatus.CREATED);
        logger.info("Product variant created successfully");
        return response;
    }

    @PutMapping
    @Operation(summary = "Update product", description = "Used to update the product")
    public ResponseEntity<SuccessResponse> updateProductVariant(@Valid @RequestBody ProductVariantUpdateRequest productVariantUpdateRequest) {
        logger.info("Received request to update product variant");
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(productVariantService.updateProductVariant(productVariantUpdateRequest), HttpStatus.OK);
        logger.info("Product variant updated successfully");
        return response;
    }

    @DeleteMapping
    @Operation(summary = "Delete product", description = "Used to delete the product")
    public ResponseEntity<SuccessResponse> deleteProductVariant(@RequestParam("productVariantUid") String productVariantUid) {
        logger.info("Received request to delete product variant with productVariantUid: {}", productVariantUid);
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(productVariantService.deleteProductVariant(productVariantUid), HttpStatus.NO_CONTENT);
        logger.info("Product variant deleted successfully with productVariantUid: {}", productVariantUid);
        return response;
    }

}