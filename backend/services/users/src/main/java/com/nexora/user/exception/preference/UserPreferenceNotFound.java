package com.nexora.user.exception.preference;

import com.nexora.user.response.preference.UserPreferenceResponse;

public class UserPreferenceNotFound extends RuntimeException {
    public UserPreferenceNotFound(String uid) {
        super("User preference not found with uid " + uid + "");
    }
}
