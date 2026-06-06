package com.nexora.user.profile.controller;

import com.nexora.user.profile.service.UserProfileService;
import com.nexora.user.request.user.UpdateUserProfileRequest;
import com.nexora.user.request.user.UserCreationRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.user.UserProfileResponse;
import com.nexora.user.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(IUrls.USER + IUrls.PROFILE)
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfileResponse> createUserProfile(@RequestBody UserCreationRequest userCreationRequest) {
        return new ResponseEntity<>(userProfileService.createUserProfile(userCreationRequest), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Update profile", description = "Used to update the user profile")
    public ResponseEntity<SuccessResponse<String>> updateUserProfile(@Valid  UpdateUserProfileRequest updateUserProfileRequest) {
        return new ResponseEntity<>(userProfileService.updateUserProfile(updateUserProfileRequest), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Fetch profile", description = "Used to fetch user specific profile")
    public ResponseEntity<UserProfileResponse> fetchUserProfile(@RequestParam("userProfileUid") String userProfileUid) {
        return new ResponseEntity<>(userProfileService.fetchUserProfile(userProfileUid), HttpStatus.OK);
    }

}
