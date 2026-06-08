package com.nexora.orders.order.service;

import com.nexora.orders.exception.order.EmptyOrderList;
import com.nexora.orders.order.model.Orders;
import com.nexora.orders.order.repository.OrderRepository;
import com.nexora.orders.response.order.OrderResponse;
import com.nexora.orders.utility.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderResponse> fetchAllOrders(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        sortBy = sortBy == null ? "status" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        Page<Orders> ordersPage = orderRepository.findAll(pageable);
        if (ordersPage.isEmpty()) {
            throw new EmptyOrderList();
        }

        return ordersPage.getContent().stream().map(GlobalUtility::convertFromOrderToOrderResponse).toList();

    }
}
