package com.nexora.user.profile.service;

import com.nexora.common.events.UserCreatedEvent;
import com.nexora.user.exception.profile.UserProfileNotFound;
import com.nexora.user.kafka.enums.EventType;
import com.nexora.user.kafka.producer.UserCreatedEventProducer;
import com.nexora.user.profile.model.UserProfile;
import com.nexora.user.profile.repository.UserProfileRepository;
import com.nexora.user.request.user.UpdateUserProfileRequest;
import com.nexora.user.request.user.UserCreationRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.user.UserProfileResponse;
import com.nexora.user.utility.GlobalUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private static final Logger log = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserCreatedEventProducer userCreatedEventProducer;

    @Override
    @Transactional
    public void createUserProfile(UserCreationRequest userCreationRequest) {
        log.info("Request received to create user profile for email: {}", userCreationRequest.email());
        UserProfile userProfile = GlobalUtils.convertFromUserProfileRequestToUserProfile(userCreationRequest);
        UserProfile savedProfile = userProfileRepository.save(userProfile);
        log.debug("User profile saved to database with UID: {}", savedProfile.getUid());

        log.debug("Publishing USER_CREATED Kafka event for user target: {}", savedProfile.getUserUid());
        userCreatedEventProducer.publishUserCreatedEventNotification(UserCreatedEvent.builder()
                .eventType(EventType.USER_CREATED)
                .email(savedProfile.getEmail())
                .username(savedProfile.getFirstName())
                .userUid(savedProfile.getUserUid())
                .build());

        GlobalUtils.convertFromUserProfileToUserProfileResponse(savedProfile);
        log.info("User profile creation flow completed successfully for UID: {}", savedProfile.getUid());
    }

    @Override
    public SuccessResponse<String> updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest) {
        log.info("Request received to update user profile for target UID: {}", updateUserProfileRequest.userProfileUid());
        UserProfile userProfile = getUserProfile(updateUserProfileRequest.userProfileUid());

        if (updateUserProfileRequest.bio() != null && !updateUserProfileRequest.bio().isBlank()) {
            log.trace("Updating profile bio field");
            userProfile.setBio(updateUserProfileRequest.bio());
        }
        if (updateUserProfileRequest.profileImageUrl() != null && !updateUserProfileRequest.profileImageUrl().isBlank()) {
            log.trace("Updating profile image URL field");
            userProfile.setProfileImageUrl(userProfile.getProfileImageUrl());
        }
        if (updateUserProfileRequest.firstName() != null && !updateUserProfileRequest.firstName().isBlank()) {
            log.trace("Updating profile first name field");
            userProfile.setFirstName(updateUserProfileRequest.firstName());
        }
        if (updateUserProfileRequest.lastName() != null && !updateUserProfileRequest.lastName().isBlank()) {
            log.trace("Updating profile last name field");
            userProfile.setLastName(updateUserProfileRequest.lastName());
        }
        if (updateUserProfileRequest.phoneNumber() != null) {
            log.trace("Updating profile phone number field");
            userProfile.setPhoneNumber(updateUserProfileRequest.phoneNumber());
        }

        userProfileRepository.save(userProfile);
        log.info("User profile with UID: {} updated successfully", updateUserProfileRequest.userProfileUid());

        return new SuccessResponse<>("User profile has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    @Transactional
    public UserProfileResponse fetchUserProfile(String userProfileUid) {
        log.debug("Fetching user profile data for UID: {}", userProfileUid);
        UserProfile userProfile = getUserProfile(userProfileUid);
        return GlobalUtils.convertFromUserProfileToUserProfileResponse(userProfile);
    }

    @Override
    public void isProfileExists(String userProfileUid) {
        log.trace("Checking structural existence of user profile UID: {}", userProfileUid);
        boolean isExist = userProfileRepository.existsByUid(userProfileUid);
        if (!isExist) {
            log.warn("Existence validation failed. Profile UID: {} does not exist", userProfileUid);
            throw new UserProfileNotFound(userProfileUid);
        }
    }

    private UserProfile getUserProfile(String userProfileUid) {
        String userUid = GlobalUtils.getLoggedInUserDetails().userUid();
        log.trace("Querying user profile via Composite Keys - ProfileUID: {}, UserUID: {}", userProfileUid, userUid);
        return userProfileRepository.findByUidAndUserUid(userProfileUid, userUid).orElseThrow(() -> {
            log.warn("Profile fetch failed. No record found for ProfileUID: {} linked to UserUID: {}", userProfileUid, userUid);
            return new UserProfileNotFound(userProfileUid);
        });
    }
}