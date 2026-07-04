package com.nexora.auth.response.user;

import lombok.Builder;

import java.time.LocalDateTime;
@Builder
public record RegisterResponse(

        String uid,
        String username,
        String email,
        LocalDateTime createdAt

) {
}