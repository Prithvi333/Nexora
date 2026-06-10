package com.nexora.orders.orderItems.service;
import com.nexora.orders.config.feign.products.ProductClient;
import com.nexora.orders.exception.order.OrderNotFound;
import com.nexora.orders.exception.orderItems.EmptyOrderItemList;
import com.nexora.orders.exception.orderItems.OrderItemNotFound;
import com.nexora.orders.order.model.Orders;
import com.nexora.orders.order.repository.OrderRepository;
import com.nexora.orders.orderItems.model.OrderItem;
import com.nexora.orders.orderItems.repository.OrderItemRepository;
import com.nexora.orders.request.orderItems.UpdateOrderItemRequest;
import com.nexora.orders.response.SuccessResponse;
import com.nexora.orders.response.orderItems.OrderItemResponse;
import com.nexora.orders.utility.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class OrderItemServiceImpl implements OrderItemService {
    private static final Logger logger = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;


    @Override
    public List<OrderItemResponse> fetchOrderItems(String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering fetchOrderItems with orderUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", orderUid, pageNo, pageSize, sortBy, direction);
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        logger.info("Fetching order for orderUid: {} and userUid: {}", orderUid, userUid);
        Orders order = orderRepository.findByUidAndUserUid(orderUid, userUid).orElseThrow(() -> new OrderNotFound(orderUid));
        sortBy = sortBy == null ? "price" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        logger.info("Fetching order items for orderUid: {}", orderUid);
        Page<OrderItem> orderItemPage = orderItemRepository.findByOrder(order, pageable);

        if (orderItemPage.isEmpty()) {
            logger.warn("No order items found for orderUid: {}", orderUid);
            throw new EmptyOrderItemList();
        }

        logger.info("Fetched {} order items successfully for orderUid: {}", orderItemPage.getContent().size(), orderUid);
        return orderItemPage.getContent().stream().map(GlobalUtility::convertFromOrderItemToOrderItemResponse).toList();

    }

    @Override
    public SuccessResponse updateOrderItem(UpdateOrderItemRequest updateOrderItemRequest) {
        logger.info("Entering updateOrderItem with orderItemUid: {}", updateOrderItemRequest.orderItemUid());
        OrderItem orderItem = findOrderItem(updateOrderItemRequest.orderItemUid());
        logger.info("Checking product quantity for variantUid: {} with requested quantity: {}", orderItem.getVariantUid(), updateOrderItemRequest.quantity());
        productClient.checkQuantity(orderItem.getVariantUid(), updateOrderItemRequest.quantity());
        orderItem.setQuantity(updateOrderItemRequest.quantity());
        orderItemRepository.save(orderItem);
        logger.info("Order item updated successfully with orderItemUid: {}", updateOrderItemRequest.orderItemUid());
        return new SuccessResponse("Order item has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse deleteOrderItem(String itemUid) {
        logger.info("Entering deleteOrderItem with itemUid: {}", itemUid);
        OrderItem orderItem = findOrderItem(itemUid);
        orderItemRepository.delete(orderItem);
        logger.info("Order item deleted successfully with itemUid: {}", itemUid);
        return new SuccessResponse("Order item has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private OrderItem findOrderItem(String itemUid) {
        logger.info("Entering findOrderItem with itemUid: {}", itemUid);
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        logger.info("Fetching order item for itemUid: {} and userUid: {}", itemUid, userUid);
        return orderItemRepository.findByUidAndOrder_UserUid(itemUid, userUid).orElseThrow(() -> new OrderItemNotFound(itemUid));
    }

}