package com.nexora.user.profile.service;

import com.nexora.user.exception.profile.EmptyUserProfileList;
import com.nexora.user.profile.model.UserProfile;
import com.nexora.user.profile.repository.UserProfileRepository;
import com.nexora.user.response.user.UserProfileResponse;
import com.nexora.user.utility.GlobalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserProfileServiceImpl implements AdminUserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Override
    public List<UserProfileResponse> fetchAllUserProfiles(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        sortBy = sortBy.isEmpty() ? "username" : sortBy;
        Pageable pageable = GlobalUtils.getPageable(pageNo, pageSize, sortBy, direction);

        Page<UserProfile> userProfilePage = userProfileRepository.findAll(pageable);
        if (userProfilePage.isEmpty()) {
            throw new EmptyUserProfileList();
        }

        return userProfilePage.getContent().stream().map(GlobalUtils::convertFromUserProfileToUserProfileResponse).toList();

    }
}
