package com.nexora.payment.payment.service;

import com.nexora.payment.request.payment.CreatePaymentRequest;


public interface PaymentService {

    void makePayment(CreatePaymentRequest createPaymentRequest);

}
