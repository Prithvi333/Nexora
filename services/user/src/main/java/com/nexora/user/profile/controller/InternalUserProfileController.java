package com.nexora.user.profile.controller;

import com.nexora.user.profile.service.UserProfileService;
import com.nexora.user.utility.constants.IUrls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(IUrls.INTERNAL)
public class InternalUserProfileController {
    private static final Logger logger = LoggerFactory.getLogger(InternalUserProfileController.class);

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/user/profile/exists")
    public void isUserProfileExists(@RequestParam("userProfileUid") String userProfileUid) {
        logger.info("Received request to check if user profile exists for userProfileUid: {}", userProfileUid);
        userProfileService.isProfileExists(userProfileUid);
        logger.info("User profile existence check completed for userProfileUid: {}", userProfileUid);
    }

}