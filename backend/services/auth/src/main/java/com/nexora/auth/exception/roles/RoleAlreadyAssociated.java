package com.nexora.auth.exception.roles;

public class RoleAlreadyAssociated extends RuntimeException {
    public RoleAlreadyAssociated(String roleName, String userName) {
        super("Role with name " + roleName + " already associated with " + userName + "");
    }
}
