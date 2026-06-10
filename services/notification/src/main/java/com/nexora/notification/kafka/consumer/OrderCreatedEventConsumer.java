package com.nexora.notification.kafka.consumer;

import com.nexora.common.events.OrderCreatedEvent;
import com.nexora.notification.notification.service.NotificationService;
import com.nexora.notification.request.notification.NotificationRequest;
import com.nexora.notification.utils.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventConsumer {


    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = ITopics.ORDER_CREATED)
    public void consumeOrderCreatedEvent(OrderCreatedEvent orderCreatedEvent) {
        notificationService.sendNotification(NotificationRequest.builder()
                .email(orderCreatedEvent.email())
                .eventType(orderCreatedEvent.eventType())
                .userUid(orderCreatedEvent.userUid())
                .message("You order has been created with order-id " + orderCreatedEvent.orderUid() + " \n Total amount to be paid is " + orderCreatedEvent.amount() + "")
                .build());
    }

}
