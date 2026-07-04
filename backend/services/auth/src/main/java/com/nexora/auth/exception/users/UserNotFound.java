package com.nexora.auth.exception.users;

public class UserNotFound extends RuntimeException {

    public UserNotFound() {
        super("User not found");
    }

    public UserNotFound(String message) {
        super("User not found with uid " + message);
    }

}
