package com.nexora.user.preference.service;

import com.nexora.user.exception.preference.CurrencyNotFound;
import com.nexora.user.exception.preference.LanguageNotFound;
import com.nexora.user.exception.preference.UserPreferenceNotFound;
import com.nexora.user.exception.profile.UserProfileNotFound;
import com.nexora.user.preference.enums.CurrencyType;
import com.nexora.user.preference.enums.Language;
import com.nexora.user.preference.model.UserPreference;
import com.nexora.user.preference.repository.UserPreferenceRepository;
import com.nexora.user.profile.model.UserProfile;
import com.nexora.user.profile.repository.UserProfileRepository;
import com.nexora.user.request.preference.CreateUserPreferenceRequest;
import com.nexora.user.request.preference.UpdateUserPreferenceRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.preference.UserPreferenceResponse;
import com.nexora.user.utility.GlobalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public UserPreferenceResponse createUserPreference(CreateUserPreferenceRequest userPreferenceRequest) {
        UserProfile userProfile = userProfileRepository.findByUid(userPreferenceRequest.userUid()).orElseThrow(() -> new UserProfileNotFound(userPreferenceRequest.userUid()));
        UserPreference.UserPreferenceBuilder userPreferenceBuilder = GlobalUtils.convertFromUserPreferenceRequestToUserPreference(userPreferenceRequest);
        userPreferenceBuilder.userUid(userProfile.getUserUid());
        return GlobalUtils.convertFromUserPreferenceToUserPreferenceResponse(userPreferenceRepository.save(userPreferenceBuilder.build()));
    }

    @Override
    public UserPreferenceResponse fetchPreferences(String preferenceUid) {
        UserPreference userPreference = getUserPreference(preferenceUid);
        return GlobalUtils.convertFromUserPreferenceToUserPreferenceResponse(userPreference);
    }

    @Override
    public SuccessResponse<String> updatePreference(UpdateUserPreferenceRequest updateUserPreferenceRequest) {
        UserPreference userPreference = getUserPreference(updateUserPreferenceRequest.userPreferenceUid());
        if (updateUserPreferenceRequest.pushNotifications() != null) {
            userPreference.setPushNotifications(updateUserPreferenceRequest.pushNotifications());
        }
        if (updateUserPreferenceRequest.smsNotifications() != null) {
            userPreference.setSmsNotifications(updateUserPreferenceRequest.smsNotifications());
        }
        if (updateUserPreferenceRequest.emailNotifications() != null) {
            userPreference.setEmailNotifications(updateUserPreferenceRequest.emailNotifications());
        }
        if (updateUserPreferenceRequest.language() != null) {
            if (!Language.isExist(updateUserPreferenceRequest.language())) {
                throw new LanguageNotFound(updateUserPreferenceRequest.language());
            }
        }

        if (updateUserPreferenceRequest.currency() != null) {
            if (!CurrencyType.isExist(updateUserPreferenceRequest.currency())) {
                throw new CurrencyNotFound(updateUserPreferenceRequest.currency());
            }
        }
        userPreferenceRepository.save(userPreference);

        return new SuccessResponse<>("User preference updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse<String> deleteUserPreference(String preferenceUid) {
        UserPreference userPreference = getUserPreference(preferenceUid);
        userPreferenceRepository.delete(userPreference);

        return new SuccessResponse<>("User preference has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private UserPreference getUserPreference(String preferenceUid) {
        return userPreferenceRepository.findByUid(preferenceUid).orElseThrow(() -> new UserPreferenceNotFound(preferenceUid));
    }

}
