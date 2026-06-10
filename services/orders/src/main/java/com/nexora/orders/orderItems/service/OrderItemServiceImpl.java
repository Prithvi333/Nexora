package com.nexora.orders.orderItems.service;

import com.nexora.orders.clients.feign.products.ProductClient;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;


    @Override
    public List<OrderItemResponse> fetchOrderItems(String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        Orders order = orderRepository.findByUidAndUserUid(orderUid, userUid).orElseThrow(() -> new OrderNotFound(orderUid));
        sortBy = sortBy == null ? "price" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);

        Page<OrderItem> orderItemPage = orderItemRepository.findByOrder(order, pageable);

        if (orderItemPage.isEmpty()) {
            throw new EmptyOrderItemList();
        }

        return orderItemPage.getContent().stream().map(GlobalUtility::convertFromOrderItemToOrderItemResponse).toList();

    }

    @Override
    public SuccessResponse updateOrderItem(UpdateOrderItemRequest updateOrderItemRequest) {
        OrderItem orderItem = findOrderItem(updateOrderItemRequest.orderItemUid());
        productClient.checkQuantity(orderItem.getVariantUid(), updateOrderItemRequest.quantity());
        orderItem.setQuantity(updateOrderItemRequest.quantity());
        orderItemRepository.save(orderItem);
        return new SuccessResponse("Order item has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse deleteOrderItem(String itemUid) {
        OrderItem orderItem = findOrderItem(itemUid);
        orderItemRepository.delete(orderItem);
        return new SuccessResponse("Order item has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private OrderItem findOrderItem(String itemUid) {
        String userUid = GlobalUtility.getLoggedInUserDetails().userUid();
        return orderItemRepository.findByUidAndOrder_UserUid(itemUid, userUid).orElseThrow(() -> new OrderItemNotFound(itemUid));
    }
}
