package com.nexora.user.request.user;

import lombok.Builder;

@Builder
public record UserCreationRequest(
        String userUid,
        String userName,
        String email
) {
}
