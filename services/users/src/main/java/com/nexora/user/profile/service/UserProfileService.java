package com.nexora.user.profile.service;

import com.nexora.user.request.user.UpdateUserProfileRequest;
import com.nexora.user.request.user.UserCreationRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.user.UserProfileResponse;

import java.util.List;

public interface UserProfileService {

    void createUserProfile(UserCreationRequest userCreationRequest);

    SuccessResponse<String> updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest);

    void deleteUserProfile(String userUid);

    UserProfileResponse fetchUserProfile(String userProfileUid);

    Boolean isProfileExists(String userProfileUid);

}
