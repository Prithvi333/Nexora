package com.nexora.auth.request.role;

import lombok.Builder;

@Builder
public record RoleResponse(

        String uid,

        String roleName

) {
}