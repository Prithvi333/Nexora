package com.nexora.ai.dto.users;

public enum CurrencyType {
    INR, USD, OTHER;

    public static boolean isExist(String currency) {

        if (currency == null) {
            return false;
        }

        for (CurrencyType currencyType : values()) {
            if (currencyType.name().equalsIgnoreCase(currency)) {
                return true;
            }
        }

        return false;
    }
}
