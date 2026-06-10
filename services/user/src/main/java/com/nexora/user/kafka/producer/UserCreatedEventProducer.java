package com.nexora.user.kafka.producer;

import com.nexora.common.events.UserCreatedEvent;
import com.nexora.user.utility.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public void publishUserCreatedEventNotification(UserCreatedEvent userCreatedEvent) {
        kafkaTemplate.send(ITopics.USER_CREATED_NOTIFICATION, userCreatedEvent.userUid(), userCreatedEvent);
    }
}
