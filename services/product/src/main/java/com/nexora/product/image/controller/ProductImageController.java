package com.nexora.product.image.controller;

import com.nexora.product.image.service.ProductImageService;
import com.nexora.product.request.image.ProductImageRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.image.ProductImageResponse;
import com.nexora.product.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(IUrls.ADMIN + IUrls.IMAGE)
public class ProductImageController {

    @Autowired
    private ProductImageService productImageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Save product image", description = "Used to save the product image")
    public ResponseEntity<ProductImageResponse> saveImage(
            @Valid @ModelAttribute ProductImageRequest productImageRequest
    ) {
        return new ResponseEntity<>(productImageService.saveImage(productImageRequest), HttpStatus.CREATED);
    }

    @DeleteMapping
    @Operation(summary = "Delete product image", description = "User to delete the product image")
    public ResponseEntity<SuccessResponse> deleteImage(@RequestParam("productImageUid") String productImageUid) {
        return new ResponseEntity<>(productImageService.deleteImage(productImageUid), HttpStatus.NO_CONTENT);
    }

}
