package com.nexora.payment.kafka.consumer;

import com.nexora.common.events.BaseEvent;
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

    @KafkaListener(topics = ITopics.ORDER_PAYMENT_EVENT)
    public void consumePaymentRequestEvent(BaseEvent baseEvent) {

        if (baseEvent instanceof PaymentRequestEvent paymentRequestEvent) {

            paymentService.makePayment(CreatePaymentRequest.builder()
                    .userUid(paymentRequestEvent.getUserUid())
                    .amount(paymentRequestEvent.getAmount())
                    .orderUid(paymentRequestEvent.getOrderUid())
                    .paymentMethod(paymentRequestEvent.getPaymentMethod())
                    .currency(paymentRequestEvent.getCurrency())
                    .build());
        }

    }

}
