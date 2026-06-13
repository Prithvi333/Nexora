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
import jakarta.transaction.Transactional;
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
    public List<OrderItemResponse> fetchOrderItems(String userProfileUId, String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        logger.info("Entering fetchOrderItems with orderUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", orderUid, pageNo, pageSize, sortBy, direction);
        logger.info("Fetching order for orderUid: {} and userUid: {}", orderUid, userProfileUId);
        Orders order = orderRepository.findByUidAndUserProfileUid(orderUid, userProfileUId).orElseThrow(() -> new OrderNotFound(orderUid));
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
    @Transactional
    public SuccessResponse updateOrderItem(UpdateOrderItemRequest updateOrderItemRequest) {
        logger.info("Entering updateOrderItem with orderItemUid: {}", updateOrderItemRequest.orderItemUid());
        OrderItem orderItem = findOrderItem(updateOrderItemRequest.userProfileUid(), updateOrderItemRequest.orderItemUid());
        logger.info("Checking product quantity for variantUid: {} with requested quantity: {}", orderItem.getVariantUid(), updateOrderItemRequest.quantity());
        productClient.checkQuantity(orderItem.getVariantUid(), updateOrderItemRequest.quantity());
        updateOrderItemQuantityAndOrderPrice(updateOrderItemRequest, orderItem);
        logger.info("Order item updated successfully with orderItemUid: {}", updateOrderItemRequest.orderItemUid());
        return new SuccessResponse("Order item has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    @Transactional
    public SuccessResponse deleteOrderItem(String userProfileUid, String itemUid) {
        logger.info("Entering deleteOrderItem with itemUid: {}", itemUid);
        OrderItem orderItem = findOrderItem(userProfileUid, itemUid);
        deleteOrderItemFromOrder(orderItem);
        logger.info("Order item deleted successfully with itemUid: {}", itemUid);
        return new SuccessResponse("Order item has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private void deleteOrderItemFromOrder(OrderItem orderItem) {

        Orders order = orderItem.getOrder();

        double reduction =
                orderItem.getPrice() * orderItem.getQuantity();

        order.setTotalAmount(
                order.getTotalAmount() - reduction
        );

        order.getItems().remove(orderItem);
        if (order.getItems().isEmpty()) {
            orderRepository.delete(order);
        } else {
            orderRepository.save(order);
        }
    }

    private void updateOrderItemQuantityAndOrderPrice(
            UpdateOrderItemRequest request,
            OrderItem orderItem) {

        int oldQty = orderItem.getQuantity();
        int newQty = request.quantity();

        if (oldQty == newQty) {
            return;
        }

        Orders order = orderItem.getOrder();

        double diff =
                Math.abs(newQty - oldQty) * orderItem.getPrice();

        if (request.increment()) {
            order.setTotalAmount(
                    order.getTotalAmount() + diff
            );
        } else {
            order.setTotalAmount(
                    order.getTotalAmount() - diff);
        }

        orderItem.setQuantity(newQty);

        orderRepository.save(order);
    }

    private OrderItem findOrderItem(String userProfileUid, String itemUid) {
        logger.info("Entering findOrderItem with itemUid: {}", itemUid);
        logger.info("Fetching order item for itemUid: {} and userProfileUid: {}", itemUid, userProfileUid);
        return orderItemRepository.findByUidAndOrder_UserProfileUid(itemUid, userProfileUid).orElseThrow(() -> new OrderItemNotFound(itemUid));
    }

}