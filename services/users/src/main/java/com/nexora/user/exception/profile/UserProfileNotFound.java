package com.nexora.user.exception.profile;

public class UserProfileNotFound extends RuntimeException {
    public UserProfileNotFound(String uid) {
        super("User profile not found with uid " + uid + "");
    }
}
