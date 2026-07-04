package com.nexora.auth.role.enums;

public enum IRoleNames {
    ADMIN, USER,CUSTOMER;

    public static boolean exists(String value) {
        for (IRoleNames role : values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
