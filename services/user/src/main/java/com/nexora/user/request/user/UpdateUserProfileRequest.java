package com.nexora.user.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateUserProfileRequest(
        @NotBlank(message = "User profile uid must be there")
        String userProfileUid,

        String firstName,

        String lastName,

        String phoneNumber,

        String profileImageUrl,

        String bio

) {
}