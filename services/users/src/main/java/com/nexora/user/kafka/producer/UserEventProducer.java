package com.nexora.user.kafka.producer;

import com.nexora.common.events.BaseEvent;
import com.nexora.user.utility.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void publishToNotificationService(BaseEvent baseEvent) {
        kafkaTemplate.send(ITopics.USER_USER_EVENT, baseEvent.getUserUid(), baseEvent);
    }


}
