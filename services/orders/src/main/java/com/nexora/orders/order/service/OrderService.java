package com.nexora.orders.order.service;

import com.nexora.orders.request.order.CreateOrderRequest;
import com.nexora.orders.response.order.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest orderRequest);

    OrderResponse fetchOrder(String orderUid);

}
