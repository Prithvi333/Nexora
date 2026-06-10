package com.nexora.orders.order.service;

import com.nexora.orders.request.order.CreateOrderRequest;
import com.nexora.orders.response.order.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest orderRequest);

    List<OrderResponse> fetchOrder(String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction);

}
