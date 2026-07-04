package com.nexora.notification.kafka.consumer;

import com.nexora.common.events.BaseEvent;
import com.nexora.common.events.UserCreatedEvent;
import com.nexora.common.events.UserDeletedEvent;
import com.nexora.notification.notification.service.NotificationService;
import com.nexora.notification.request.notification.NotificationRequest;
import com.nexora.notification.utils.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    @Autowired
    private NotificationService notificationService;


    @KafkaListener(topics = ITopics.USER_USER_EVENT)
    public void consumeUserEvents(BaseEvent baseEvent) {
        System.out.println("Sending notification for " + baseEvent.getEventType() + "");
        if (baseEvent instanceof UserCreatedEvent userCreatedEvent) {
            notificationService.sendNotification(NotificationRequest.builder()
                    .email(userCreatedEvent.getEmail())
                    .eventType(userCreatedEvent.getEventType())
                    .userUid(userCreatedEvent.getUserUid())
                    .message("User has been created with the username " + userCreatedEvent.getUsername() + "")
                    .build());
        }
        if (baseEvent instanceof UserDeletedEvent userDeletedEvent) {
            notificationService.sendNotification(NotificationRequest.builder()
                    .email(userDeletedEvent.getEmail())
                    .eventType(userDeletedEvent.getEventType())
                    .userUid(userDeletedEvent.getUserUid())
                    .message("User has been deleted with the username " + userDeletedEvent.getUsername() + "")
                    .build());
        }

    }

}
