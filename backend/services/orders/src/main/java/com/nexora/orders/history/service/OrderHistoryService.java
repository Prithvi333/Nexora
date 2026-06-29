package com.nexora.orders.history.service;

import com.nexora.orders.history.model.OrderHistory;
import com.nexora.orders.response.history.OrderHistoryResponse;

import java.util.List;

public interface OrderHistoryService {
    void createOrderHistory(OrderHistory orderHistory);

    List<OrderHistoryResponse> orderHistoryList(String userProfileUid, String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction);
}
