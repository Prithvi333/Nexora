package com.nexora.orders.kafka.producer;

import com.nexora.common.events.OrderCreatedEvent;
import com.nexora.orders.utility.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public void publishOrderEvent(OrderCreatedEvent orderCreatedEvent) {
        kafkaTemplate.send(ITopics.ORDER_ORDER_EVENT, orderCreatedEvent.getUserUid(), orderCreatedEvent);
    }
}
