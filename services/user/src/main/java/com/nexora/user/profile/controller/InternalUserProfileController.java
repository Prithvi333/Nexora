package com.nexora.user.profile.controller;

import com.nexora.user.profile.service.UserProfileService;
import com.nexora.user.utility.constants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(IUrls.INTERNAL)
public class InternalUserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/user/profile/exists")
    public void isUserProfileExists(String userProfileUid) {
        userProfileService.isProfileExists(userProfileUid);
    }

}
