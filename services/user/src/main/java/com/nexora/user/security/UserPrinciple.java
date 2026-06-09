package com.nexora.user.security;

public record UserPrinciple(
        String userUid,
        String username,
        String role
) {
}
