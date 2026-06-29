package com.nexora.payment.kafka.producer;

import com.nexora.common.events.BaseEvent;
import com.nexora.common.events.PaymentStatusEvent;
import com.nexora.payment.utility.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.stream.BaseStream;

@Component
public class PaymentStatusEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishOrderStatusEvent(BaseEvent baseEvent) {
        kafkaTemplate.send(ITopics.PAYMENT_PAYMENT_STATUS_EVENT, baseEvent.getUserUid(), baseEvent);
    }

}
