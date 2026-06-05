package com.nexora.auth.exception.roles;

public class EmptyRoleList extends RuntimeException {
    public EmptyRoleList() {
        super("Role list is empty");
    }
}
