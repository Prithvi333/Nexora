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
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PreferenceServiceImpl implements PreferenceService {

    private static final Logger log = LoggerFactory.getLogger(PreferenceServiceImpl.class);

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public UserPreferenceResponse createUserPreference(CreateUserPreferenceRequest userPreferenceRequest) {
        log.info("Attempting to create user preference for Profile UID: {}", userPreferenceRequest.userProfileUid());

        if (userPreferenceRequest.language() != null && !Language.isExist(userPreferenceRequest.language().toUpperCase())) {
            log.warn("Invalid language provided: {}", userPreferenceRequest.language());
            throw new LanguageNotFound("Language not found with name " + userPreferenceRequest.language() + "");
        }
        if (userPreferenceRequest.currency() != null && !CurrencyType.isExist(userPreferenceRequest.currency().toUpperCase())) {
            log.warn("Invalid currency provided: {}", userPreferenceRequest.currency());
            throw new LanguageNotFound("Currency not found with name " + userPreferenceRequest.currency() + "");
        }

        UserProfile userProfile = userProfileRepository.findByUid(userPreferenceRequest.userProfileUid())
                .orElseThrow(() -> {
                    log.error("Profile not found for UID: {}", userPreferenceRequest.userProfileUid());
                    return new UserProfileNotFound(userPreferenceRequest.userProfileUid());
                });

        UserPreference.UserPreferenceBuilder userPreferenceBuilder = GlobalUtils.convertFromUserPreferenceRequestToUserPreference(userPreferenceRequest);
        userPreferenceBuilder.userUid(userProfile.getUserUid());
        userPreferenceBuilder.userProfile(userProfile);
        UserPreference toSaveUserPreference = userPreferenceBuilder.build();
        userProfile.getPreferences().add(toSaveUserPreference);

        log.info("User preference created and associated with User UID: {}", userProfile.getUserUid());
        return GlobalUtils.convertFromUserPreferenceToUserPreferenceResponse(toSaveUserPreference);
    }

    @Override
    public List<UserPreferenceResponse> fetchPreferences(String preferenceUid, Integer pageNo, Integer pageSize, String sortBy, String direction) {
        log.debug("Fetching preferences. Preference UID: {}, Page: {}, Size: {}", preferenceUid, pageNo, pageSize);


        if (preferenceUid != null) {
            UserPreference userPreference = getUserPreference(preferenceUid);
            return List.of(GlobalUtils.convertFromUserPreferenceToUserPreferenceResponse(userPreference));
        }

        String userUid = GlobalUtils.getLoggedInUserDetails().userUid();
        sortBy = sortBy == null ? "language" : sortBy;
        Pageable pageable = GlobalUtils.getPageable(pageNo, pageSize, sortBy, direction);
        Page<UserPreference> preferencePage = userPreferenceRepository.findByUserUid(userUid, pageable);

        log.debug("Retrieved {} preferences for user", preferencePage.getNumberOfElements());
        return preferencePage.getContent().stream().map(GlobalUtils::convertFromUserPreferenceToUserPreferenceResponse).toList();
    }

    @Override
    public SuccessResponse<String> updatePreference(UpdateUserPreferenceRequest updateUserPreferenceRequest) {
        log.info("Updating user preference UID: {}", updateUserPreferenceRequest.userPreferenceUid());
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
        if (userPreference.getDefaultPreference() != null) {
            userPreference.setDefaultPreference(updateUserPreferenceRequest.defaultPreference());
        }
        if (updateUserPreferenceRequest.language() != null) {
            if (!Language.isExist(updateUserPreferenceRequest.language().toUpperCase())) {
                log.warn("Update failed: Invalid language '{}'", updateUserPreferenceRequest.language());
                throw new LanguageNotFound(updateUserPreferenceRequest.language());
            }
            userPreference.setLanguage(Language.valueOf(updateUserPreferenceRequest.language().toUpperCase()));
        }

        if (updateUserPreferenceRequest.currency() != null) {
            if (!CurrencyType.isExist(updateUserPreferenceRequest.currency().toUpperCase())) {
                log.warn("Update failed: Invalid currency '{}'", updateUserPreferenceRequest.currency());
                throw new CurrencyNotFound(updateUserPreferenceRequest.currency());
            }
            userPreference.setCurrency(CurrencyType.valueOf(updateUserPreferenceRequest.currency().toUpperCase()));
        }

        userPreferenceRepository.save(userPreference);
        log.info("Successfully updated preference UID: {}", updateUserPreferenceRequest.userPreferenceUid());

        return new SuccessResponse<>("User preference updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse<String> deleteUserPreference(String preferenceUid) {
        log.info("Deleting preference UID: {}", preferenceUid);
        UserPreference userPreference = getUserPreference(preferenceUid);
        userPreferenceRepository.delete(userPreference);
        log.info("Preference UID: {} deleted successfully", preferenceUid);

        return new SuccessResponse<>("User preference has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    private UserPreference getUserPreference(String preferenceUid) {
        String userUid = GlobalUtils.getLoggedInUserDetails().userUid();
        log.trace("Retrieving preference for User UID: {} and Pref UID: {}", userUid, preferenceUid);
        return userPreferenceRepository.findByUidAndUserUid(preferenceUid, userUid)
                .orElseThrow(() -> {
                    log.warn("Preference not found for UID: {}", preferenceUid);
                    return new UserPreferenceNotFound(preferenceUid);
                });
    }
}