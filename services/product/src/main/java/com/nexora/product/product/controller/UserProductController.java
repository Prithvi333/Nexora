package com.nexora.product.product.controller;

import com.nexora.product.product.service.UserProductService;
import com.nexora.product.response.product.ProductResponse;
import com.nexora.product.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(IUrls.USER + IUrls.PRODUCT)
public class UserProductController {

    @Autowired
    private UserProductService userProductService;

    @GetMapping
    @Transactional
    @Operation(summary = "Fetch product", description = "Used to get all the products")
    public ResponseEntity<List<ProductResponse>> getProducts(@RequestParam(required = false) String productUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(userProductService.getProducts(productUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

}
