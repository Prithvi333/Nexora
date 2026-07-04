package com.nexora.payment.payment.service;

import com.nexora.payment.request.payment.CreatePaymentRequest;
import com.nexora.payment.response.payment.PaymentResponse;


public interface PaymentService {

    void makePayment(CreatePaymentRequest createPaymentRequest);

    PaymentResponse getPaymentResponseByOrderUid(String orderUid);
}
