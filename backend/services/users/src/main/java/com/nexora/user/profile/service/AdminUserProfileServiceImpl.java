package com.nexora.user.profile.service;

import com.nexora.user.exception.profile.EmptyUserProfileList;
import com.nexora.user.profile.model.UserProfile;
import com.nexora.user.profile.repository.UserProfileRepository;
import com.nexora.user.response.user.UserProfileResponse;
import com.nexora.user.utility.GlobalUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserProfileServiceImpl implements AdminUserProfileService {

    private static final Logger log = LoggerFactory.getLogger(AdminUserProfileServiceImpl.class);

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public List<UserProfileResponse> fetchAllUserProfiles(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        log.debug("Request received to fetch all user profiles - Page: {}, Size: {}, SortBy: {}, Direction: {}", pageNo, pageSize, sortBy, direction);

        sortBy = sortBy == null ? "firstName" : sortBy;
        Pageable pageable = GlobalUtils.getPageable(pageNo, pageSize, sortBy, direction);

        Page<UserProfile> userProfilePage = userProfileRepository.findAll(pageable);
        if (userProfilePage.isEmpty()) {
            log.warn("No user profiles found for the specified pagination criteria.");
            throw new EmptyUserProfileList();
        }

        log.info("Successfully retrieved {} user profiles.", userProfilePage.getNumberOfElements());
        return userProfilePage.getContent().stream().map(GlobalUtils::convertFromUserProfileToUserProfileResponse).toList();
    }
}