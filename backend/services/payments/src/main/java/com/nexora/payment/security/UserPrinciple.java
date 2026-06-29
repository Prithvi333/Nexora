package com.nexora.payment.security;

public record UserPrinciple(
        String userUid,
        String username,
        String role
) {
}

