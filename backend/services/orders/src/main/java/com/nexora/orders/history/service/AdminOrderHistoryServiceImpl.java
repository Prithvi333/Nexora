package com.nexora.orders.history.service;
import com.nexora.orders.exception.history.EmptyOrderHistoryList;
import com.nexora.orders.history.model.OrderHistory;
import com.nexora.orders.history.repository.OrderHistoryRepository;
import com.nexora.orders.response.history.OrderHistoryResponse;
import com.nexora.orders.utility.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AdminOrderHistoryServiceImpl implements AdminOrderHistoryService {
    private static final Logger logger = LoggerFactory.getLogger(AdminOrderHistoryServiceImpl.class);

    @Autowired
    private OrderHistoryRepository orderHistoryRepository;

    @Override
    public List<OrderHistoryResponse> fetchAllOrderHistory(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering fetchAllOrderHistory with pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);
        sortBy = sortBy == null ? "timestamp" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        logger.info("Fetching all order history with pagination");
        Page<OrderHistory> orderHistoryPage = orderHistoryRepository.findAll(pageable);

        if (orderHistoryPage.isEmpty()) {
            logger.warn("No order history found");
            throw new EmptyOrderHistoryList();
        }
        logger.info("Fetched {} order history records successfully", orderHistoryPage.getContent().size());
        return orderHistoryPage.getContent().stream().map(GlobalUtility::convertFromOderHistoryToOrderHistoryResponse).toList();
    }

}