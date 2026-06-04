package com.nexora.auth.request.token;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDate;
@Builder
public record CreateRefreshTokenRequest(

        @NotBlank(message = "User UID is required")
        String userUid,

        @NotBlank(message = "Token is required")
        String token,

        LocalDate expiryDate

) {
}