package com.nexora.product.product.controller;

import com.nexora.product.product.service.UserProductService;
import com.nexora.product.request.product.OrderItemRequest;
import com.nexora.product.request.variant.VariantPriceResponse;
import com.nexora.product.utility.constants.IUrls;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(IUrls.INTERNAL + IUrls.PRODUCT)
public class InternalProductController {


    @Autowired
    private UserProductService userProductService;

    @PostMapping("/validate")
    public List<VariantPriceResponse> getProducts(@Valid @RequestBody List<OrderItemRequest> orderItemRequest) {
        return userProductService.getProductsByOrderItem(orderItemRequest);
    }
}
