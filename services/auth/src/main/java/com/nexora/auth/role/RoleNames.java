package com.nexora.auth.role;

import com.nexora.auth.role.model.Roles;

public enum RoleNames {
    ROLE_ADMIN, ROLE_USER;

    public static boolean exists(String value) {
        for (RoleNames role : values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
