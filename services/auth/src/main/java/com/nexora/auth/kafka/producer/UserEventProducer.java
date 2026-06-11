package com.nexora.auth.kafka.producer;

import com.nexora.auth.utils.contants.ITopics;
import com.nexora.common.events.BaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserEvent(BaseEvent baseEvent) {
        kafkaTemplate.send(ITopics.AUTH_USER_EVENT, baseEvent.getUserUid(), baseEvent);
    }

}
