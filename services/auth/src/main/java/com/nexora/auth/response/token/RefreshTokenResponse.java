package com.nexora.auth.response.token;

public record RefreshTokenResponse(

        String accessToken,
        String refreshToken

) {
}