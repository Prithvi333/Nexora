package com.nexora.orders.exception.order;

public class EmptyOrderList extends RuntimeException {
    public EmptyOrderList() {
        super("Empty order items list");
    }
}
