package com.nexora.payment.payment.service;

import com.nexora.payment.kafka.event.CreatePaymentEvent;
import com.nexora.payment.response.payment.PaymentResponse;

public interface PaymentService {

    PaymentResponse makePayment(CreatePaymentEvent paymentEvent);

}
