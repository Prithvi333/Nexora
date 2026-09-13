package com.nexora.user.profile.controller;

import com.nexora.user.profile.service.UserProfileService;
import com.nexora.user.request.user.UpdateUserProfileRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.user.UserProfileResponse;
import com.nexora.user.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(IUrls.USER + IUrls.PROFILE)
public class UserProfileController {
    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserProfileService userProfileService;

    @PutMapping
    @Operation(summary = "Update profile", description = "Used to update the user profile")
    public ResponseEntity<SuccessResponse<String>> updateUserProfile(@Valid @RequestBody UpdateUserProfileRequest updateUserProfileRequest) {
        logger.info("Received request to update user profile");
        ResponseEntity<SuccessResponse<String>> response = new ResponseEntity<>(userProfileService.updateUserProfile(updateUserProfileRequest), HttpStatus.OK);
        logger.info("User profile updated successfully");
        return response;
    }

    @GetMapping
    @Operation(summary = "Fetch profile", description = "Used to fetch user specific profile")
    public ResponseEntity<UserProfileResponse> fetchUserProfile(@RequestParam("userProfileUid") String userProfileUid) {
        logger.info("Received request to fetch user profile with userProfileUid: {}", userProfileUid);
        ResponseEntity<UserProfileResponse> response = new ResponseEntity<>(userProfileService.fetchUserProfile(userProfileUid), HttpStatus.OK);
        logger.info("User profile fetched successfully for userProfileUid: {}", userProfileUid);
        return response;
    }

}