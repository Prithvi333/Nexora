package com.nexora.auth.role.service;

import com.nexora.auth.exception.roles.EmptyRoleList;
import com.nexora.auth.exception.roles.RoleAlreadyAssociated;
import com.nexora.auth.exception.roles.RoleAlreadyExist;
import com.nexora.auth.exception.roles.RoleNotFound;
import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.request.role.CreateRoleRequest;
import com.nexora.auth.request.role.RoleResponse;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.role.enums.IRoleNames;
import com.nexora.auth.role.model.Roles;
import com.nexora.auth.role.repository.RoleRepository;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import com.nexora.auth.utils.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RoleResponse createRole(CreateRoleRequest createRoleRequest) {
        log.info("Request received to create role with name: {}", createRoleRequest.roleName());
        String role = createRoleRequest.roleName().toUpperCase();
        boolean isRoleExist = IRoleNames.exists(role);
        if (!isRoleExist) {
            log.warn("Role creation failed. Role name {} is invalid according to IRoleNames", role);
            throw new RoleNotFound("Role not found with role name " + role + " ");
        }
        isRoleAlreadyExist(role);
        RoleResponse response = convertFromRoleToRoleResponse(roleRepository.save(convertFromRoleRequestToRole(createRoleRequest)));
        log.info("Successfully created role with UID: {}", response.uid());
        return response;
    }

    @Override
    public RoleResponse getRoleByUid(String uid) {
        log.debug("Fetching role details for UID: {}", uid);
        return convertFromRoleToRoleResponse(findRoleByUid(uid));
    }

    @Override
    public List<RoleResponse> getAllRoles(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        log.debug("Fetching all roles - Page: {}, Size: {}, SortBy: {}, Direction: {}", pageNo, pageSize, sortBy, direction);
        sortBy = sortBy == null ? "roleName" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Roles> page = roleRepository.findAll(pageable);
        if (page.isEmpty()) {
            log.warn("No roles found for the requested pagination settings");
            throw new EmptyRoleList();
        }
        return page.getContent().stream().map(this::convertFromRoleToRoleResponse).toList();
    }

    @Override
    public SuccessResponse deleteRole(String roleUid) {
        log.info("Request received to delete role with UID: {}", roleUid);
        Roles role = findRoleByUid(roleUid);
        roleRepository.delete(role);
        log.info("Role with UID: {} deleted successfully", roleUid);
        return new SuccessResponse("Role with uid " + roleUid + " has been deleted successfully", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    @Override
    public SuccessResponse updateRole(String userUid, String roleUid, Boolean assign) {
        log.info("Processing role modification request. User UID: {}, Role UID: {}, Assign operation: {}", userUid, roleUid, assign);
        Optional<Users> optionalUsers = userRepository.findByUidAndEnabledTrue(userUid);
        if (optionalUsers.isEmpty()) {
            log.warn("Role update aborted. Active user not found with UID: {}", userUid);
            throw new UserNotFound("User not found with uid " + userUid + "");
        }
        Optional<Roles> optionalRoles = roleRepository.findByUid(roleUid);
        if (optionalRoles.isEmpty()) {
            log.warn("Role update aborted. Role not found with UID: {}", roleUid);
            throw new RoleNotFound("Role not found with uid " + roleUid + "");
        }

        Users user = optionalUsers.get();
        Roles role = optionalRoles.get();
        boolean isExist = user.getRoles().stream().anyMatch(roles -> roles.getUid().equalsIgnoreCase(roleUid));
        if (assign) {
            if (isExist) {
                log.warn("Failed to assign role. Role '{}' already associated with user '{}'", role.getRoleName(), user.getUsername());
                throw new RoleAlreadyAssociated(role.getRoleName(), user.getUsername());
            }
            user.getRoles().add(role);
            log.debug("Adding role '{}' to user '{}'", role.getRoleName(), user.getUsername());
        } else {
            if (!isExist) {
                log.warn("Failed to unassign role. Role '{}' is not associated with user '{}'", role.getRoleName(), user.getUsername());
                throw new RoleNotFound("Role with name " + role.getRoleName() + " not associated with user with username " + user.getUsername() + "");
            }
            user.getRoles().removeIf(roles -> roles.getUid().equalsIgnoreCase(roleUid));
            log.debug("Removing role '{}' from user '{}'", role.getRoleName(), user.getUsername());
        }

        userRepository.save(user);
        log.info("Role association successfully updated for user UID: {}", userUid);
        return new SuccessResponse("Role update successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    private Roles findRoleByUid(String uid) {
        Optional<Roles> optionalRole = roleRepository.findByUid(uid);
        if (optionalRole.isEmpty()) {
            throw new RoleNotFound("Role not found with uid " + uid + "");
        }
        return optionalRole.get();
    }

    private Roles convertFromRoleRequestToRole(CreateRoleRequest roleRequest) {
        return Roles.builder().uid(UUID.randomUUID().toString()).roleName("ROLE_" + roleRequest.roleName().toUpperCase()).build();
    }

    private void isRoleAlreadyExist(String roleName) {
        roleName = "ROLE_" + roleName;
        Optional<Roles> roles = roleRepository.findByRoleName(roleName);
        if (roles.isPresent()) {
            throw new RoleAlreadyExist(roleName.substring(5));
        }
    }

    private RoleResponse convertFromRoleToRoleResponse(Roles roles) {
        return RoleResponse.builder().uid(roles.getUid()).roleName(roles.getRoleName()).build();
    }
}