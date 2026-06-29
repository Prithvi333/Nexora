package com.nexora.orders.exception.order;

public class OrderNotFound extends RuntimeException {
    public OrderNotFound(String orderUid) {
        super("Order not found with uid " + orderUid + "");
    }
}
