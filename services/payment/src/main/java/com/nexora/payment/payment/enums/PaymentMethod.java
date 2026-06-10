package com.nexora.payment.payment.enums;

public enum PaymentMethod {
    CARD,
    UPI,
    NET_BANKING,
    WALLET;

    public static boolean isValid(String value) {
        if (value == null || value.isBlank()) return false;
        try {
            valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}