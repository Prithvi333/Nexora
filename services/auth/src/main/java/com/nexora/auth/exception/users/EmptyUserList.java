package com.nexora.auth.exception.users;

public class EmptyUserList extends RuntimeException {

    public EmptyUserList() {
        super("User list is empty");
    }
}
