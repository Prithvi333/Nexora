package com.nexora.user.profile.controller;

import com.nexora.user.profile.service.UserProfileService;
import com.nexora.user.request.user.UpdateUserProfileRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.user.UserProfileResponse;
import com.nexora.user.utility.constants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(IUrls.USER + IUrls.PROFILE)
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PutMapping
    public ResponseEntity<SuccessResponse<String>> updateUserProfile(UpdateUserProfileRequest updateUserProfileRequest) {
        return new ResponseEntity<>(userProfileService.updateUserProfile(updateUserProfileRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<UserProfileResponse> fetchUserProfile(@RequestParam("userProfileUid") String userProfileUid) {
        return new ResponseEntity<>(userProfileService.fetchUserProfile(userProfileUid), HttpStatus.OK);
    }

}
