package com.nexora.orders.order.controller;

import com.nexora.orders.order.service.OrderService;
import com.nexora.orders.request.order.CreateOrderRequest;
import com.nexora.orders.response.order.OrderResponse;
import com.nexora.orders.utility.constants.IUrls;
import jakarta.validation.Valid;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(IUrls.USER + IUrls.ORDER)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        return new ResponseEntity<>(orderService.createOrder(createOrderRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<OrderResponse> fetchOrder(@RequestParam("orderUid") String orderUid) {
        return new ResponseEntity<>(orderService.fetchOrder(orderUid), HttpStatus.OK);
    }
}
