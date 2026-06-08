package com.nexora.orders.order.service;

import com.nexora.orders.request.order.CreateOrderRequest;
import com.nexora.orders.response.order.OrderResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest orderRequest);

    OrderResponse fetchOrder(String orderUid);

}
