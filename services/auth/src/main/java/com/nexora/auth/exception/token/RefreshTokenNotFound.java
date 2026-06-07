package com.nexora.auth.exception.token;

public class RefreshTokenNotFound extends RuntimeException {
    public RefreshTokenNotFound() {
        super("Refresh token not found");
    }
}
