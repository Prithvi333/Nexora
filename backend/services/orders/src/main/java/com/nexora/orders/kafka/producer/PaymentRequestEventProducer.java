package com.nexora.orders.kafka.producer;

import com.nexora.common.events.BaseEvent;
import com.nexora.common.events.PaymentRequestEvent;
import com.nexora.orders.utility.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentRequestEvent(BaseEvent baseEvent) {
        kafkaTemplate.send(ITopics.ORDER_PAYMENT_EVENT, baseEvent.getUserUid(), baseEvent);
    }

}
