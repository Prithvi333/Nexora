package com.nexora.user.profile.service;

import com.nexora.user.exception.profile.UserProfileNotFound;
import com.nexora.user.profile.model.UserProfile;
import com.nexora.user.profile.repository.UserProfileRepository;
import com.nexora.user.request.user.UpdateUserProfileRequest;
import com.nexora.user.request.user.UserCreationRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.user.UserProfileResponse;
import com.nexora.user.utility.GlobalUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public UserProfileResponse createUserProfile(UserCreationRequest userCreationRequest) {
        UserProfile userProfile = GlobalUtils.convertFromUserProfileRequestToUserProfile(userCreationRequest);
        return GlobalUtils.convertFromUserProfileToUserProfileResponse(userProfileRepository.save(userProfile));
    }

    @Override
    public SuccessResponse<String> updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest) {
        UserProfile userProfile = getUserProfile(updateUserProfileRequest.userProfileUid());
        if (updateUserProfileRequest.bio() != null && !updateUserProfileRequest.bio().isBlank()) {
            userProfile.setBio(updateUserProfileRequest.bio());
        }
        if (updateUserProfileRequest.profileImageUrl() != null && !updateUserProfileRequest.profileImageUrl().isBlank()) {
            userProfile.setProfileImageUrl(userProfile.getProfileImageUrl());
        }
        if (updateUserProfileRequest.firstName() != null && !updateUserProfileRequest.firstName().isBlank()) {
            userProfile.setFirstName(updateUserProfileRequest.firstName());
        }
        if (updateUserProfileRequest.lastName() != null && !updateUserProfileRequest.lastName().isBlank()) {
            userProfile.setLastName(updateUserProfileRequest.lastName());
        }
        if (updateUserProfileRequest.phoneNumber() != null) {
            userProfile.setPhoneNumber(updateUserProfileRequest.phoneNumber());
        }

        userProfileRepository.save(userProfile);

        return new SuccessResponse<>("User profile has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());


    }

    @Override
    @Transactional
    public UserProfileResponse fetchUserProfile(String userProfileUid) {
        UserProfile userProfile = getUserProfile(userProfileUid);
        return GlobalUtils.convertFromUserProfileToUserProfileResponse(userProfile);
    }

    @Override
    public void isProfileExists(String userProfileUid) {
        boolean isExist = userProfileRepository.existsByUid(userProfileUid);
        if (!isExist) {
            throw new UserProfileNotFound(userProfileUid);
        }
    }

    private UserProfile getUserProfile(String userProfileUid) {
        String userUid = GlobalUtils.getLoggedInUserDetails().userUid();
        return userProfileRepository.findByUidAndUserUid(userProfileUid, userUid).orElseThrow(() -> new UserProfileNotFound(userProfileUid));
    }
}
