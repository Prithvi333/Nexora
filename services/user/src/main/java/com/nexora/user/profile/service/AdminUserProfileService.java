package com.nexora.user.profile.service;

import com.nexora.user.response.user.UserProfileResponse;

import java.util.List;

public interface AdminUserProfileService {

    List<UserProfileResponse> fetchAllUserProfiles(Integer pageNo, Integer pageSize, String sortBy, String direction);
}
