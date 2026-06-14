package com.nexora.notification.security;

public record UserPrinciple(
        String userUid,
        String username,
        String role
) {
}
