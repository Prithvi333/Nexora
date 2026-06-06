package com.nexora.user.preference.controller;

import com.nexora.user.preference.service.PreferenceService;
import com.nexora.user.request.preference.CreateUserPreferenceRequest;
import com.nexora.user.request.preference.UpdateUserPreferenceRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.preference.UserPreferenceResponse;
import com.nexora.user.utility.constants.IUrls;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(IUrls.USER + IUrls.PREFERENCE)
public class PreferenceController {

    @Autowired
    private PreferenceService preferenceService;

    @PostMapping
    public ResponseEntity<UserPreferenceResponse> createUserPreference(@Valid @RequestBody CreateUserPreferenceRequest createUserPreferenceRequest) {
        return new ResponseEntity<>(preferenceService.createUserPreference(createUserPreferenceRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserPreferenceResponse> fetchPreferences(@RequestParam("preferenceUid") String preferenceUid) {
        return new ResponseEntity<>(preferenceService.fetchPreferences(preferenceUid), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SuccessResponse<String>> updatePreference(@Valid @RequestBody UpdateUserPreferenceRequest userPreferenceRequest) {
        return new ResponseEntity<>(preferenceService.updatePreference(userPreferenceRequest), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse<String>> deleteUserPreference(@RequestParam("preferenceUid") String preferenceUid) {
        return new ResponseEntity<>(preferenceService.deleteUserPreference(preferenceUid), HttpStatus.NO_CONTENT);
    }

}

