package com.nexora.notification.kafka.consumer;

import com.nexora.common.events.BaseEvent;
import com.nexora.common.events.OrderCreatedEvent;
import com.nexora.notification.notification.service.NotificationService;
import com.nexora.notification.request.notification.NotificationRequest;
import com.nexora.notification.utils.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {


    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = ITopics.ORDER_ORDER_EVENT)
    public void consumeOrderCreatedEvent(BaseEvent baseEvent) {

        if (baseEvent instanceof OrderCreatedEvent orderCreatedEvent) {

            notificationService.sendNotification(NotificationRequest.builder()
                    .email(orderCreatedEvent.getEmail())
                    .eventType(orderCreatedEvent.getEventType())
                    .userUid(orderCreatedEvent.getUserUid())
                    .message("You order has been created with order-id " + orderCreatedEvent.getOrderUid() + " \n Total amount to be paid is " + orderCreatedEvent.getAmount() + "")
                    .build());
        }
    }

}
