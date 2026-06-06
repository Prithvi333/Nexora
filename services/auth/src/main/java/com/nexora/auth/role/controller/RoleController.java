package com.nexora.auth.role.controller;

import com.nexora.auth.request.role.CreateRoleRequest;
import com.nexora.auth.request.role.RoleResponse;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.role.service.RoleService;
import com.nexora.auth.utils.contants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(IUrls.ROLE)
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    @Operation(summary = "Create role", description = "Used to create the role")
    ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest createRoleRequest) {
        return new ResponseEntity<>(roleService.createRole(createRoleRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("{roleUid}")
    @Operation(summary = "Delete role", description = "Used to delete the role")
    ResponseEntity<SuccessResponse> deleteRole(@PathVariable("roleUid") String roleUid) {
        return new ResponseEntity<>(roleService.deleteRole(roleUid), HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(summary = "Fetch roles", description = "User to get the role")
    ResponseEntity<List<RoleResponse>> getRoles(@RequestParam(required = false) String roleUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        if (roleUid != null && !roleUid.isBlank()) {
            return new ResponseEntity<>(List.of(roleService.getRoleByUid(roleUid)), HttpStatus.OK);
        }
        return new ResponseEntity<>(roleService.getAllRoles(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }


    @PostMapping(IUrls.UPDATE_USER_ROLE)
    @Operation(summary = "Update User Role", description = "User to allocate or deallocate the role")
    ResponseEntity<SuccessResponse> updateUserRole(@RequestParam String userUid, @RequestParam String roleUid, @RequestParam Boolean assign) {
        return new ResponseEntity<>(roleService.updateRole(userUid, roleUid, assign), HttpStatus.OK);
    }

}
