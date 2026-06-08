package com.nexora.orders.history.service;

import com.nexora.orders.exception.history.EmptyOrderHistoryList;
import com.nexora.orders.history.model.OrderHistory;
import com.nexora.orders.history.repository.OrderHistoryRepository;
import com.nexora.orders.response.history.OrderHistoryResponse;
import com.nexora.orders.utility.GlobalUtility;
import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrderHistoryServiceImpl implements AdminOrderHistoryService {

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Override
    public List<OrderHistoryResponse> fetchAllOrderHistory(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        sortBy = sortBy == null ? "timestamp" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        Page<OrderHistory> orderHistoryPage = orderHistoryRepository.findAll(pageable);

        if (orderHistoryPage.isEmpty()) {
            throw new EmptyOrderHistoryList();
        }
        return orderHistoryPage.getContent().stream().map(GlobalUtility::convertFromOderHistoryToOrderHistoryResponse).toList();
    }
}
