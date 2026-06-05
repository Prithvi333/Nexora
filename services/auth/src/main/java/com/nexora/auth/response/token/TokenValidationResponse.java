package com.nexora.auth.response.token;

import lombok.Builder;

@Builder
public record TokenValidationResponse(
        Boolean valid,
        String username,
        String roles,
        String newToken
) {
}
