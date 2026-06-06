package com.nexora.user.profile.controller;

import com.nexora.user.profile.service.AdminUserProfileService;
import com.nexora.user.response.user.UserProfileResponse;
import com.nexora.user.utility.constants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(IUrls.ADMIN + IUrls.PROFILE)
public class AdminUserProfileController {

    @Autowired
    private AdminUserProfileService adminUserProfileService;

    @GetMapping
    public ResponseEntity<List<UserProfileResponse>> fetchAllUserProfile(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(adminUserProfileService.fetchAllUserProfiles(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }
}
