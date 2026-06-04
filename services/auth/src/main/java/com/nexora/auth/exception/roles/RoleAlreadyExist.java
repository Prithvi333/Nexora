package com.nexora.auth.exception.roles;

public class RoleAlreadyExist extends RuntimeException {
    public RoleAlreadyExist(String roleName) {
        super("Role already exist with name " + roleName + "");
    }
}
