package com.nexora.orders.exception.orderItems;

public class EmptyOrderItemList extends RuntimeException {
    public EmptyOrderItemList() {
        super("Empty order items list");
    }
}
