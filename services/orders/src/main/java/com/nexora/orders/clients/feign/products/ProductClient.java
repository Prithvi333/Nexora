package com.nexora.orders.clients.feign.products;

import com.nexora.orders.request.orderItems.OrderItemRequest;
import com.nexora.orders.response.order.VariantPriceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "products"
)
public interface ProductClient {

    @PostMapping("/internal/product/validate")
    List<VariantPriceResponse> getProducts(@RequestBody List<OrderItemRequest> orderItemRequests);

    @GetMapping("/internal/quantity/validate/{variantUid}/{quantity}")
    VariantPriceResponse checkQuantity(@PathVariable("variantUid") String variantUid, @PathVariable("quantity") Integer quantity);

}
