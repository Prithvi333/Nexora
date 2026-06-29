package com.nexora.orders.history.service;

import com.nexora.orders.response.history.OrderHistoryResponse;

import java.util.List;

public interface AdminOrderHistoryService {

    List<OrderHistoryResponse> fetchAllOrderHistory(Integer pageNo, Integer pageSize, String sortBy, String direction);
}
