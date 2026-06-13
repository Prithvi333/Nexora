package com.nexora.auth.security;

public record UserPrinciple(
        String userUid,
        String username,
        String role
) {
}
