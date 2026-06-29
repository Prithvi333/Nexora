package com.nexora.auth.response.user;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record UserResponse(

        String uid,

        String username,

        String email,

        Set<String> roles,

        LocalDateTime createdAt

) {
}