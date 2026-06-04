package com.nexora.auth.role.service;

import com.nexora.auth.request.role.CreateRoleRequest;
import com.nexora.auth.request.role.RoleResponse;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.role.model.Roles;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(CreateRoleRequest createRoleRequest);

    RoleResponse getRoleByUid(String uid);

    List<RoleResponse> getAllRoles(Integer pageNo, Integer pageSize, String sortBy, String direction);

    SuccessResponse deleteRole(String roleUid);

    SuccessResponse updateRole(String userUid, String roleUid, Boolean assign);

}
