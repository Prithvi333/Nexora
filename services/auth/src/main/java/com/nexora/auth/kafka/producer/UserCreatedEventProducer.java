package com.nexora.auth.kafka.producer;

import com.nexora.common.events.UserCreatedEvent;
import com.nexora.auth.utils.contants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public void publishUserCreatedEvent(UserCreatedEvent userCreatedEvent) {
        kafkaTemplate.send(ITopics.USER_CREATED, userCreatedEvent.userUid(), userCreatedEvent);
    }

}
