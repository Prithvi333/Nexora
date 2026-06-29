package com.nexora.auth.exception.token;

public class RefreshTokenExpired extends RuntimeException {
    public RefreshTokenExpired() {
        super("Refresh token has already expired,Please sign in again");
    }
}
