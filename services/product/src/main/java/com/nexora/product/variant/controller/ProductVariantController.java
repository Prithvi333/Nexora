package com.nexora.product.variant.controller;

import com.nexora.product.request.variant.ProductVariantRequest;
import com.nexora.product.request.variant.ProductVariantUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.variant.ProductVariantResponse;
import com.nexora.product.utility.constants.IUrls;
import com.nexora.product.variant.service.ProductVariantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(IUrls.ADMIN + IUrls.VARIANT)
public class ProductVariantController {

    @Autowired
    private ProductVariantService productVariantService;

    @PostMapping
    public ResponseEntity<ProductVariantResponse> createProductVariant(@Valid @RequestBody ProductVariantRequest productVariantRequest) {
        return new ResponseEntity<>(productVariantService.createProductVariant(productVariantRequest), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse> updateProductVariant(@Valid @RequestBody ProductVariantUpdateRequest productVariantUpdateRequest) {
        return new ResponseEntity<>(productVariantService.updateProductVariant(productVariantUpdateRequest), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteProductVariant(@RequestParam("productVariantUid") String productVariantUid) {
        return new ResponseEntity<>(productVariantService.deleteProductVariant(productVariantUid), HttpStatus.NO_CONTENT);
    }


}
