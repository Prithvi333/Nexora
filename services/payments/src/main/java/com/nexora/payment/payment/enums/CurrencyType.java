package com.nexora.payment.payment.enums;

public enum CurrencyType {
    INR, USD;

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
