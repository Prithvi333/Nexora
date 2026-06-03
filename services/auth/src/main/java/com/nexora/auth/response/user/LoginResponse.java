package com.nexora.auth.response.user;

import java.util.Set;

public record LoginResponse(

        String accessToken,
        String refreshToken,

        Long userId,
        String username,
        String email,

        Set<String> roles

) {
}