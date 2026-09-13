package com.nexora.payment.exception.payment;

public class PaymentException extends RuntimeException {

    public PaymentException() {
        super("Unable to process the payment");
    }

    public PaymentException(String message) {
        super(message);
    }
}
