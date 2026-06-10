package com.nexora.orders.order.controller;
import com.nexora.orders.order.service.OrderService;
import com.nexora.orders.request.order.CreateOrderRequest;
import com.nexora.orders.request.order.PaymentRequest;
import com.nexora.orders.response.SuccessResponse;
import com.nexora.orders.response.order.OrderResponse;
import com.nexora.orders.utility.constants.IUrls;
import jakarta.validation.Valid;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping(IUrls.USER + IUrls.ORDER)
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        logger.info("Received request to create order");
        ResponseEntity<OrderResponse> response = new ResponseEntity<>(orderService.createOrder(createOrderRequest), HttpStatus.CREATED);
        logger.info("Order created successfully");
        return response;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> fetchOrder(@RequestParam(required = false) String orderUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        logger.info("Received request to fetch order with orderUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", orderUid, pageNo, pageSize, sortBy, direction);
        ResponseEntity<List<OrderResponse>> response = new ResponseEntity<>(orderService.fetchOrder(orderUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("Order fetched successfully");
        return response;
    }

    @PostMapping(IUrls.PAYMENT)
    public ResponseEntity<SuccessResponse> createPayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        logger.info("Received request to create payment for orderUid: {} and userUid: {}", paymentRequest.orderUid(), paymentRequest.userUid());
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(orderService.createPayment(paymentRequest), HttpStatus.OK);
        logger.info("Payment request created successfully for orderUid: {}", paymentRequest.orderUid());
        return response;
    }

}