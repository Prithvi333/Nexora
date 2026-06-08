package com.nexora.orders.orderItems.service;

import com.nexora.orders.request.orderItems.UpdateOrderItemRequest;
import com.nexora.orders.response.SuccessResponse;
import com.nexora.orders.response.orderItems.OrderItemResponse;

import java.util.List;

public interface OrderItemService {

    List<OrderItemResponse> fetchOrderItems(String orderUid, Integer pageNo, Integer pageSize, String sortBy, String direction);

    SuccessResponse updateOrderItem(UpdateOrderItemRequest updateOrderItemRequest);

    SuccessResponse deleteOrderItem(String itemUid);

}
