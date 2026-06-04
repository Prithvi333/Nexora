package com.nexora.auth.response.token;

import lombok.Builder;

@Builder
public record TokenResponse(
        String accessToken,
        String refreshToken

) {
}
