package com.nexora.product.security;

public record UserPrinciple(
        String userUid,
        String username,
        String role
) {
}
