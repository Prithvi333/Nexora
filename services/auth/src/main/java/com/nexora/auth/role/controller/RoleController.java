package com.nexora.auth.role.controller;
import com.nexora.auth.request.role.CreateRoleRequest;
import com.nexora.auth.request.role.RoleResponse;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.role.service.RoleService;
import com.nexora.auth.utils.contants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping(IUrls.ADMIN + IUrls.ROLE)
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @PostMapping
    @Operation(summary = "Create role", description = "Used to create the role")
    ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest createRoleRequest) {
        logger.info("Received request to create role with name: {}", createRoleRequest.roleName());
        ResponseEntity<RoleResponse> response = new ResponseEntity<>(roleService.createRole(createRoleRequest), HttpStatus.CREATED);
        logger.info("Role created successfully with name: {}", createRoleRequest.roleName());
        return response;
    }

    @DeleteMapping("{roleUid}")
    @Operation(summary = "Delete role", description = "Used to delete the role")
    ResponseEntity<SuccessResponse> deleteRole(@PathVariable("roleUid") String roleUid) {
        logger.info("Received request to delete role with roleUid: {}", roleUid);
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(roleService.deleteRole(roleUid), HttpStatus.NO_CONTENT);
        logger.info("Role deleted successfully with roleUid: {}", roleUid);
        return response;
    }

    @GetMapping
    @Operation(summary = "Fetch roles", description = "User to get the role")
    ResponseEntity<List<RoleResponse>> getRoles(@RequestParam(required = false) String roleUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        logger.info("Received request to fetch roles with roleUid: {}, pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", roleUid, pageNo, pageSize, sortBy, direction);
        if (roleUid != null && !roleUid.isBlank()) {
            logger.info("Fetching role by roleUid: {}", roleUid);
            ResponseEntity<List<RoleResponse>> response = new ResponseEntity<>(List.of(roleService.getRoleByUid(roleUid)), HttpStatus.OK);
            logger.info("Role fetched successfully with roleUid: {}", roleUid);
            return response;
        }
        ResponseEntity<List<RoleResponse>> response = new ResponseEntity<>(roleService.getAllRoles(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("All roles fetched successfully");
        return response;
    }


    @PutMapping
    @Operation(summary = "Update User Role", description = "User to allocate or deallocate the role")
    ResponseEntity<SuccessResponse> updateUserRole(@RequestParam String userUid, @RequestParam String roleUid, @RequestParam Boolean assign) {
        logger.info("Received request to update user role with userUid: {}, roleUid: {}, assign: {}", userUid, roleUid, assign);
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(roleService.updateRole(userUid, roleUid, assign), HttpStatus.OK);
        logger.info("User role updated successfully for userUid: {} and roleUid: {}", userUid, roleUid);
        return response;
    }

}