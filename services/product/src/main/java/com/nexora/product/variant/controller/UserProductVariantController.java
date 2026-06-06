package com.nexora.product.variant.controller;

import com.nexora.product.response.variant.ProductVariantResponse;
import com.nexora.product.utility.constants.IUrls;
import com.nexora.product.variant.service.UserProductVariantService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(IUrls.USER + IUrls.VARIANT)
public class UserProductVariantController {

    @Autowired
    private UserProductVariantService userProductVariantService;

    @GetMapping
    @Operation(summary = "Fetch product", description = "Used to get all the product")
    public ResponseEntity<List<ProductVariantResponse>> getProductVariant(@RequestParam(required = false) String productVariantUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(userProductVariantService.getProductVariant(productVariantUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }


}
