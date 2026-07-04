package com.nexora.user.kafka.consumer;

import com.nexora.common.events.BaseEvent;
import com.nexora.common.events.UserCreatedEvent;
import com.nexora.common.events.UserDeletedEvent;
import com.nexora.user.profile.service.UserProfileService;
import com.nexora.user.request.user.UserCreationRequest;
import com.nexora.user.utility.constants.ITopics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventConsumer {

    @Autowired
    private UserProfileService userProfileService;


    @KafkaListener(topics = ITopics.AUTH_USER_EVENT)
    public void consumeUserCreatedEven(BaseEvent baseEvent) {

        if (baseEvent instanceof UserCreatedEvent event) {
            userProfileService.createUserProfile(UserCreationRequest.builder()
                    .userUid(event.getUserUid())
                    .userName(event.getUsername())
                    .email(event.getEmail())
                    .build());
        } else if (baseEvent instanceof UserDeletedEvent userDeletedEvent) {
            userProfileService.deleteUserProfile(userDeletedEvent.getUserUid());
        }


    }

}
