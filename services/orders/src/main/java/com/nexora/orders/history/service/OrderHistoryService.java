package com.nexora.orders.history.service;

import com.nexora.orders.response.history.OrderHistoryResponse;

import java.util.List;

public interface OrderHistoryService {
    List<OrderHistoryResponse> orderHistoryList(String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction);
}
