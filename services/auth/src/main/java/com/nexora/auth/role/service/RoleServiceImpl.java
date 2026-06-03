package com.nexora.auth.role.service;

import com.nexora.auth.exception.roles.EmptyRoleList;
import com.nexora.auth.exception.roles.RoleNotFound;
import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.request.role.CreateRoleRequest;
import com.nexora.auth.request.role.RoleResponse;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.role.RoleNames;
import com.nexora.auth.role.model.Roles;
import com.nexora.auth.role.repository.RoleRepository;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import com.nexora.auth.utils.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RoleResponse createRole(CreateRoleRequest createRoleRequest) {
        String role = createRoleRequest.roleName().toUpperCase();
        boolean isRoleExist = RoleNames.exists(role);
        if (!isRoleExist) {
            throw new RoleNotFound("Role not found with role name " + role + " ");
        }
        return convertFromRoleToRoleResponse(roleRepository.save(convertFromRoleRequestToRole(createRoleRequest)));
    }

    @Override
    public RoleResponse getRoleByUid(String uid) {
        return convertFromRoleToRoleResponse(findRoleByUid(uid));
    }

    @Override
    public List<RoleResponse> getAllRoles(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Roles> page = roleRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new EmptyRoleList();
        }
        return page.getContent().stream().map(this::convertFromRoleToRoleResponse).toList();
    }

    @Override
    public SuccessResponse deleteRole(String roleUid) {
        Roles role = findRoleByUid(roleUid);
        roleRepository.delete(role);
        return new SuccessResponse("Role with uid " + roleUid + " has been deleted successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse assignRole(String userUid, String roleUid) {
        Optional<Users> optionalUsers = userRepository.findByUidAndEnabledTrue(userUid);
        if (optionalUsers.isEmpty()) {
            throw new UserNotFound("User not found with uid " + userUid + "");
        }
        Optional<Roles> optionalRoles = roleRepository.findByUid(roleUid);
        if (optionalRoles.isEmpty()) {
            throw new RoleNotFound("Role not found with uid " + roleUid + "");
        }

        Users user = optionalUsers.get();
        Roles role = optionalRoles.get();

        user.getRoles().add(role);

        userRepository.save(user);
        return new SuccessResponse("" + role.getRoleName() + " has been assigned to the user with username " + user.getUsername() + " ", HttpStatus.OK.value(), LocalDateTime.now());
    }

    private Roles findRoleByUid(String uid) {
        Optional<Roles> optionalRole = roleRepository.findByUid(uid);
        if (optionalRole.isEmpty()) {
            throw new RoleNotFound("Role not found with uid " + uid + "");
        }
        return optionalRole.get();
    }

    private Roles convertFromRoleRequestToRole(CreateRoleRequest roleRequest) {
        return Roles.builder().roleName(roleRequest.roleName()).build();
    }

    private RoleResponse convertFromRoleToRoleResponse(Roles roles) {
        return RoleResponse.builder().uid(roles.getUid()).roleName(roles.getRoleName()).build();
    }
}
