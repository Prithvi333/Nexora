package com.nexora.orders.exception.order;

public class InvalidOrderStatus extends RuntimeException {
    public InvalidOrderStatus(String status) {
        super("Invalid order status with name " + status);
    }
}
