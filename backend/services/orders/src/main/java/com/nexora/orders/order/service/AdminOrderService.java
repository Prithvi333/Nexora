package com.nexora.orders.order.service;

import com.nexora.orders.response.order.OrderResponse;

import java.util.List;

public interface AdminOrderService {

    List<OrderResponse> fetchAllOrders(Integer pageNo, Integer pageSize, String sortBy, String direction);
}
