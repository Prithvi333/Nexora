package com.nexora.payment.kafka.consumer;

import com.nexora.common.events.PaymentRequestEvent;
import com.nexora.payment.payment.service.PaymentService;
import com.nexora.payment.request.payment.CreatePaymentRequest;
import com.nexora.payment.utility.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestEventConsumer {

    @Autowired
    private PaymentService paymentService;

    @KafkaListener(topics = ITopics.PAYMENT_REQUEST)
    public void consumePaymentRequestEvent(PaymentRequestEvent paymentRequestEvent) {
        paymentService.makePayment(CreatePaymentRequest.builder()
                .userUid(paymentRequestEvent.userUid())
                .orderUid(paymentRequestEvent.orderUid())
                .paymentMethod(paymentRequestEvent.paymentMethod())
                .currency(paymentRequestEvent.currency())
                .build());

    }

}
