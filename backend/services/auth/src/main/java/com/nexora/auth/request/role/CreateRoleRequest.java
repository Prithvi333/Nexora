package com.nexora.auth.request.role;

import jakarta.validation.constraints.NotBlank;

public record CreateRoleRequest(

        @NotBlank(message = "Role name is required")
        String roleName

) {
}
