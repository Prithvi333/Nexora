package com.nexora.orders.order.enums;


import java.util.Arrays;

public enum OrderStatus {
    CREATED,
    CONFIRMED,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED;

    public static boolean isValid(String status) {

        if (status == null || status.isBlank()) {
            return false;
        }

        return Arrays.stream(OrderStatus.values())
                .anyMatch(orderStatus ->
                        orderStatus.name().equalsIgnoreCase(status.toUpperCase()));
    }
}