package com.nexora.auth.request.role;

import com.nexora.auth.user.model.Users;
import lombok.Builder;

import java.util.Set;

@Builder
public record RoleResponse(

        String uid,

        String roleName

) {
}