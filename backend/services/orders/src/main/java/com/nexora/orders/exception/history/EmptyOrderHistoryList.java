package com.nexora.orders.exception.history;

public class EmptyOrderHistoryList extends RuntimeException {
    public EmptyOrderHistoryList() {
        super("Empty order history list");
    }
}
