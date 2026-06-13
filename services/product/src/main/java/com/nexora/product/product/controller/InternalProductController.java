package com.nexora.product.product.controller;
import com.nexora.product.product.service.UserProductService;
import com.nexora.product.request.product.OrderItemRequest;
import com.nexora.product.request.variant.VariantPriceResponse;
import com.nexora.product.utility.constants.IUrls;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(IUrls.INTERNAL + IUrls.PRODUCT)
public class InternalProductController {
    private static final Logger logger = LoggerFactory.getLogger(InternalProductController.class);

    @Autowired
    private UserProductService userProductService;

    @PostMapping("/validate")
    public List<VariantPriceResponse> getProducts(@Valid @RequestBody List<OrderItemRequest> orderItemRequest) {
        logger.info("Received request to validate products for {} order items", orderItemRequest.size());
        List<VariantPriceResponse> response = userProductService.getProductsByOrderItem(orderItemRequest);
        logger.info("Product validation completed successfully for {} order items", orderItemRequest.size());
        return response;
    }

}