package com.nexora.orders.security;

public record UserPrinciple(
        String userUid,
        String username,
        String role
) {
}
