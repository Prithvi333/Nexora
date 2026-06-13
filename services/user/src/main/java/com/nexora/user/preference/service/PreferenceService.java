package com.nexora.user.preference.service;

import com.nexora.user.preference.model.UserPreference;
import com.nexora.user.request.preference.CreateUserPreferenceRequest;
import com.nexora.user.request.preference.UpdateUserPreferenceRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.preference.UserPreferenceResponse;

import java.util.List;

public interface PreferenceService {

    UserPreferenceResponse createUserPreference(CreateUserPreferenceRequest userPreferenceRequest);

    List<UserPreferenceResponse> fetchPreferences(String preferenceUid, Integer pageNo, Integer pageSize, String sortBy, String direction);

    SuccessResponse<String> updatePreference(UpdateUserPreferenceRequest userPreferenceRequest);

    SuccessResponse<String> deleteUserPreference(String preferenceUid);

}
