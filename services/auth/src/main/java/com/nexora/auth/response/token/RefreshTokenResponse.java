package com.nexora.auth.response.token;

import lombok.Builder;

@Builder
public record RefreshTokenResponse(

        String refreshToken,
        String expiryDate

) {
}