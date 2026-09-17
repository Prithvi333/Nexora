package com.nexora.ai.feign;

import com.nexora.ai.dto.users.UserPreferenceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "users", url = "localhost:8083")

public interface UserClient {

    @GetMapping("/api/users/user/preference")
    ResponseEntity<List<UserPreferenceResponse>> fetchPreferences();

    @GetMapping("/api/users/user/profile/profileUid")
    ResponseEntity<String> fetchUserProfileUid(@RequestParam("userUid") String userUid);

}
