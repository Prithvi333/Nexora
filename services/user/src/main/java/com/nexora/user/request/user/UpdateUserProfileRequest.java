package com.nexora.user.request.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateUserProfileRequest(
        @NotBlank(message = "User profile uid must be there")
        String userProfileUid,

        @Size(max = 50, message = "First name cannot exceed 50 characters")
        String firstName,

        @Size(max = 50, message = "Last name cannot exceed 50 characters")
        String lastName,

        @Size(max = 15, message = "Phone number cannot exceed 15 characters")
        String phoneNumber,

        String profileImageUrl,

        @Size(max = 500, message = "Bio cannot exceed 500 characters")
        String bio

) {
}