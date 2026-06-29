package com.nexora.orders.exception.history;

public class OrderHistoryNotFound extends RuntimeException {
    public OrderHistoryNotFound(String orderUid) {
        super("Order history not found with uid " + orderUid + "");
    }
}
