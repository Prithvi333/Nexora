package com.nexora.payment.payment.gateway;

public interface PaymentGateway {

    String createOrder(Double amount, String currency, String receiptId) throws Exception;

    boolean verifyWebhook(String payload, String signature);

}
