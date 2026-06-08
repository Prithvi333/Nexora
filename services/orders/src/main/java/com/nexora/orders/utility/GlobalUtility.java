package com.nexora.orders.utility;

import com.nexora.orders.history.model.OrderHistory;
import com.nexora.orders.order.model.Orders;
import com.nexora.orders.orderItems.model.OrderItem;
import com.nexora.orders.response.history.OrderHistoryResponse;
import com.nexora.orders.response.order.OrderResponse;
import com.nexora.orders.response.orderItems.OrderItemResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class GlobalUtility {

    public static Pageable getPageable(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        pageSize = pageSize == null ? 5 : pageSize;
        pageNo = pageNo == null ? 0 : pageNo;
        Sort sort = direction == null ? Sort.by(sortBy).ascending() : direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNo, pageSize, sort);
    }





    public static OrderResponse convertFromOrderToOrderResponse(Orders order) {
        return OrderResponse.builder()
                .orderUid(order.getUid())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .userUid(order.getUserUid())
                .items(order.getItems().stream().map(GlobalUtility::convertFromOrderItemToOrderItemResponse).toList())
                .createdAt(order.getCreatedAt())
                .build();
    }

    public static OrderItemResponse convertFromOrderItemToOrderItemResponse(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .productUid(orderItem.getProductUid())
                .variantUid(orderItem.getVariantUid())
                .build();
    }

    public static OrderHistoryResponse convertFromOderHistoryToOrderHistoryResponse(OrderHistory orderHistory) {
        return OrderHistoryResponse.builder()
                .historyUid(orderHistory.getUid())
                .orderUid(orderHistory.getOrderUid())
                .fromStatus(orderHistory.getFromStatus())
                .toStatus(orderHistory.getToStatus())
                .actionBy(orderHistory.getActionBy())
                .reason(orderHistory.getReason())
                .timestamp(orderHistory.getTimestamp())
                .build();
    }

}
