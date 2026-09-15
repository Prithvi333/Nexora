package com.nexora.ai.feign;

import com.nexora.ai.dto.orders.OrderHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "orders", url = "localhost:8084")
public interface OrderClient {

    @GetMapping("/api/orders/user/history")
    ResponseEntity<List<OrderHistoryResponse>> getAllOrderHistory(@RequestParam String userProfileUid);

    @PostMapping("/api/orders/user/order/products")
    ResponseEntity<List<String>> fetchProductUidByOrderUids(@RequestBody List<String> orderUid);

}
