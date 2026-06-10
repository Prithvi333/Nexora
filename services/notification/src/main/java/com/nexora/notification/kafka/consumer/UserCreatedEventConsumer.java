package com.nexora.notification.kafka.consumer;

import com.nexora.common.events.UserCreatedEvent;
import com.nexora.notification.notification.service.NotificationService;
import com.nexora.notification.request.notification.NotificationRequest;
import com.nexora.notification.utils.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventConsumer {

    @Autowired
    private NotificationService notificationService;


    @KafkaListener(topics = ITopics.USER_CREATED_NOTIFICATION)
    public void consumeUserCreatedNotification(UserCreatedEvent userCreatedEvent) {
        notificationService.sendNotification(NotificationRequest.builder()
                .email(userCreatedEvent.email())
                .eventType(userCreatedEvent.eventType())
                .userUid(userCreatedEvent.userUid())
                .message("User has been created with the username " + userCreatedEvent.username() + "")
                .build());

    }

}
