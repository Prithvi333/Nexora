package com.nexora.auth.request.token;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record CreateRefreshTokenRequest(

        @NotBlank(message = "User UID is required")
        String userUid,

        @NotBlank(message = "Token is required")
        String token,

        LocalDateTime expiryDate

) {
}