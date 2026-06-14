package com.nexora.user.exception.profile;

public class EmptyUserProfileList extends RuntimeException {
    public EmptyUserProfileList() {
        super("User profile list is empty");
    }
}
