package com.nexora.orders.order.service;
import com.nexora.orders.exception.order.EmptyOrderList;
import com.nexora.orders.order.model.Orders;
import com.nexora.orders.order.repository.OrderRepository;
import com.nexora.orders.response.order.OrderResponse;
import com.nexora.orders.utility.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AdminOrderServiceImpl implements AdminOrderService {
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderResponse> fetchAllOrders(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering fetchAllOrders with pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);
        sortBy = sortBy == null ? "status" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        logger.info("Fetching all orders with pagination");
        Page<Orders> ordersPage = orderRepository.findAll(pageable);
        if (ordersPage.isEmpty()) {
            logger.warn("No orders found");
            throw new EmptyOrderList();
        }

        logger.info("Fetched {} orders successfully", ordersPage.getContent().size());
        return ordersPage.getContent().stream().map(GlobalUtility::convertFromOrderToOrderResponse).toList();

    }

}