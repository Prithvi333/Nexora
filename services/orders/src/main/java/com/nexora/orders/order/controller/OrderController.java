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

import java.util.List;

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
    public ResponseEntity<List<OrderResponse>> fetchOrder(@RequestParam(required = false) String orderUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(orderService.fetchOrder(orderUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }
}
