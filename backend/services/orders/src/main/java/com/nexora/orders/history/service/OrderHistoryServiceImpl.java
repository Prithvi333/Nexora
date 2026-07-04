package com.nexora.orders.history.service;

import com.nexora.orders.exception.history.EmptyOrderHistoryList;
import com.nexora.orders.exception.history.OrderHistoryNotFound;
import com.nexora.orders.exception.order.OrderNotFound;
import com.nexora.orders.history.model.OrderHistory;
import com.nexora.orders.history.repository.OrderHistoryRepository;
import com.nexora.orders.order.repository.OrderRepository;
import com.nexora.orders.response.history.OrderHistoryResponse;
import com.nexora.orders.utility.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryServiceImpl implements OrderHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(OrderHistoryServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Override
    public void createOrderHistory(OrderHistory orderHistory) {
        logger.info("Creating order history for orderUid: {}", orderHistory.getOrderUid());
        orderHistoryRepository.save(orderHistory);
        logger.info("Order history created successfully for orderUid: {}", orderHistory.getOrderUid());
    }

    @Override
    public List<OrderHistoryResponse> orderHistoryList(String userProfileUid, String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering orderHistoryList with orderUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", orderUid, pageNo, pageSize, sortBy, direction);
        sortBy = sortBy == null ? "timestamp" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        Page<OrderHistory> orderHistoryPage;
        if (orderUid != null) {
            logger.info("Fetching order history for orderUid: {}", orderUid);
            orderHistoryPage = orderHistoryRepository.findByOrderUid(orderUid, pageable);
            if (orderHistoryPage.isEmpty()) {
                logger.warn("No order history found for orderUid: {}", orderUid);
                throw new OrderHistoryNotFound(orderUid);
            }
        }

        logger.info("Fetching order history for userProfileUid: {}", userProfileUid);
        orderHistoryPage = orderHistoryRepository.findByUserProfileUid(userProfileUid, pageable);

        if (orderHistoryPage.isEmpty()) {
            logger.warn("No order history found for userProfileUid: {}", userProfileUid);
            throw new EmptyOrderHistoryList();
        }

        logger.info("Fetched {} order history records successfully for userProfileUid: {}", orderHistoryPage.getContent().size(), userProfileUid);
        return orderHistoryPage.getContent().stream().map(GlobalUtility::convertFromOderHistoryToOrderHistoryResponse).toList();

    }

}