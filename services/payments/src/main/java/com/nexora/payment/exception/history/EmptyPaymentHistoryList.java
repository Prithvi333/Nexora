package com.nexora.payment.exception.history;

public class EmptyPaymentHistoryList extends RuntimeException {

    public EmptyPaymentHistoryList() {
        super("Empty payment history list");
    }

    public EmptyPaymentHistoryList(String uid) {
        super("Payment history list is empty with user uid " + uid + "");
    }
}
