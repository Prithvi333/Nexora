package com.nexora.orders.exception.orderItems;

public class OrderItemNotFound extends RuntimeException {
    public OrderItemNotFound(String itemUid) {
        super("Order item not found with uid " + itemUid + "");
    }
}
