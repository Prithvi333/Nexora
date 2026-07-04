package com.nexora.user.exception.preference;

public class CurrencyNotFound extends RuntimeException {
    public CurrencyNotFound(String currency) {
        super("Currency not found with name " + currency + "");
    }
}
