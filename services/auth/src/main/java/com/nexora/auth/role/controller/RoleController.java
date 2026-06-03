package com.nexora.auth.role.controller;

import com.nexora.auth.request.role.CreateRoleRequest;
import com.nexora.auth.request.role.RoleResponse;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.role.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest createRoleRequest) {
        return new ResponseEntity<>(roleService.createRole(createRoleRequest), HttpStatus.CREATED);
    }

    @DeleteMapping
    ResponseEntity<SuccessResponse> deleteRole(@PathVariable("roleUid") String roleUid) {
        return new ResponseEntity<>(roleService.deleteRole(roleUid), HttpStatus.OK);
    }

    @GetMapping
    ResponseEntity<List<RoleResponse>> getRoles(@RequestParam(required = false) String roleUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        if (roleUid != null) {
            return new ResponseEntity<>(List.of(roleService.getRoleByUid(roleUid)), HttpStatus.OK);
        }
        return new ResponseEntity<>(roleService.getAllRoles(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }


    @PostMapping("/assign")
    ResponseEntity<SuccessResponse> assignRole(@RequestParam String userUid, @RequestParam String roleUid) {
        return new ResponseEntity<>(roleService.assignRole(userUid, roleUid), HttpStatus.OK);
    }

}
