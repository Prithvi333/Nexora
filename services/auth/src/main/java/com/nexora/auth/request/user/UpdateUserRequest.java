package com.nexora.auth.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        String username,
        @NotBlank
        String email,

        String currentPassword,

        @Size(min = 8)
        String newPassword,

        Boolean enabled

) {
}