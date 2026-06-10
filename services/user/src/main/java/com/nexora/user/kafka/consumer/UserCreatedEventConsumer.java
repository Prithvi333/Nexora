package com.nexora.user.kafka.consumer;

import com.nexora.common.events.UserCreatedEvent;
import com.nexora.user.profile.service.UserProfileService;
import com.nexora.user.request.user.UserCreationRequest;
import com.nexora.user.utility.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedEventConsumer {

    @Autowired
    private UserProfileService userProfileService;

    @Value("${spring.kafka.consumer.group-id}")
    private String group;

    @KafkaListener(topics = ITopics.USER_CREATED)
    public void consumeUserCreatedEven(UserCreatedEvent event) {
        System.out.println(event);
        System.out.println(event.username());
        userProfileService.createUserProfile(UserCreationRequest.builder().userUid(event.userUid())
                .userName(event.username())
                .email(event.email())
                .build());
    }

}
