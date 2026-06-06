package com.nexora.user.request.user;

public record UserCreationRequest(
        String userUid,
        String userName,
        String email
) {
}
