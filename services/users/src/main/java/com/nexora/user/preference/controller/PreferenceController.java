package com.nexora.user.preference.controller;
import com.nexora.user.preference.service.PreferenceService;
import com.nexora.user.request.preference.CreateUserPreferenceRequest;
import com.nexora.user.request.preference.UpdateUserPreferenceRequest;
import com.nexora.user.response.SuccessResponse;
import com.nexora.user.response.preference.UserPreferenceResponse;
import com.nexora.user.utility.constants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping(IUrls.USER + IUrls.PREFERENCE)
public class PreferenceController {
    private static final Logger logger = LoggerFactory.getLogger(PreferenceController.class);

    @Autowired
    private PreferenceService preferenceService;

    @PostMapping
    @Operation(summary = "Create preference", description = "Used to create the user preferences")
    public ResponseEntity<UserPreferenceResponse> createUserPreference(@Valid @RequestBody CreateUserPreferenceRequest createUserPreferenceRequest) {
        logger.info("Received request to create user preference");
        ResponseEntity<UserPreferenceResponse> response = new ResponseEntity<>(preferenceService.createUserPreference(createUserPreferenceRequest), HttpStatus.CREATED);
        logger.info("User preference created successfully");
        return response;
    }

    @GetMapping
    @Operation(summary = "Fetch preference", description = "Used to fetch the user preferences")
    public ResponseEntity<List<UserPreferenceResponse>> fetchPreferences(@RequestParam(required = false) String preferenceUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, String direction) {
        logger.info("Received request to fetch preferences with preferenceUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", preferenceUid, pageNo, pageSize, sortBy, direction);
        ResponseEntity<List<UserPreferenceResponse>> response = new ResponseEntity<>(preferenceService.fetchPreferences(preferenceUid, pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("Preferences fetched successfully");
        return response;
    }

    @PutMapping
    @Operation(summary = "Update preference", description = "Used to update the preferences")
    public ResponseEntity<SuccessResponse<String>> updatePreference(@Valid @RequestBody UpdateUserPreferenceRequest userPreferenceRequest) {
        logger.info("Received request to update user preference");
        ResponseEntity<SuccessResponse<String>> response = new ResponseEntity<>(preferenceService.updatePreference(userPreferenceRequest), HttpStatus.OK);
        logger.info("User preference updated successfully");
        return response;
    }

    @DeleteMapping
    @Operation(summary = "Delete preference", description = "Used to delete the preferences")
    public ResponseEntity<SuccessResponse<String>> deleteUserPreference(@RequestParam("preferenceUid") String preferenceUid) {
        logger.info("Received request to delete user preference with preferenceUid: {}", preferenceUid);
        ResponseEntity<SuccessResponse<String>> response = new ResponseEntity<>(preferenceService.deleteUserPreference(preferenceUid), HttpStatus.NO_CONTENT);
        logger.info("User preference deleted successfully with preferenceUid: {}", preferenceUid);
        return response;
    }

}