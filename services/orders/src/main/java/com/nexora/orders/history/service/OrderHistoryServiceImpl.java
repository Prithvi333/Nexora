package com.nexora.orders.history.service;

import com.nexora.orders.exception.history.EmptyOrderHistoryList;
import com.nexora.orders.exception.history.OrderHistoryNotFound;
import com.nexora.orders.exception.order.OrderNotFound;
import com.nexora.orders.history.model.OrderHistory;
import com.nexora.orders.history.repository.OrderHistoryRepository;
import com.nexora.orders.order.repository.OrderRepository;
import com.nexora.orders.response.history.OrderHistoryResponse;
import com.nexora.orders.utility.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Override
    public void createOrderHistory(OrderHistory orderHistory) {
        orderHistoryRepository.save(orderHistory);
    }

    @Override
    public List<OrderHistoryResponse> orderHistoryList(String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        sortBy = sortBy == null ? "timestamp" : sortBy;
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        Page<OrderHistory> orderHistoryPage;
        if (orderUid != null) {
            orderHistoryPage = orderHistoryRepository.findByOrderUid(orderUid, pageable);
            if (orderHistoryPage.isEmpty()) {
                throw new OrderHistoryNotFound(orderUid);
            }
        }

        orderHistoryPage = orderHistoryRepository.findByUserUid(userUid, pageable);

        if (orderHistoryPage.isEmpty()) {
            throw new EmptyOrderHistoryList();
        }

        return orderHistoryPage.getContent().stream().map(GlobalUtility::convertFromOderHistoryToOrderHistoryResponse).toList();

    }
}

