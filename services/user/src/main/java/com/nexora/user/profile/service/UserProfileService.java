package com.nexora.user.profile.service;

import com.nexora.user.request.user.UpdateUserProfileRequest;
import com.nexora.user.request.user.UserCreationRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.user.UserProfileResponse;

import java.util.List;

public interface UserProfileService {

    UserProfileResponse createUserProfile(UserCreationRequest userCreationRequest);

    SuccessResponse<String> updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest);

    UserProfileResponse fetchUserProfile(String userProfileUid);

    void isProfileExists(String userProfileUid);

}
