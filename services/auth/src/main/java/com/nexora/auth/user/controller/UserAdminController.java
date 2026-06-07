package com.nexora.auth.user.controller;
import com.nexora.auth.response.user.UserResponse;
import com.nexora.auth.user.service.UserAdminService;
import com.nexora.auth.utils.contants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(IUrls.ADMIN)
public class UserAdminController {

    @Autowired
    private UserAdminService userAdminService;

    @PostMapping
    @Operation(summary = "Fetch user", description = "use to fetch the user based on user Uid")
    public ResponseEntity<UserResponse> getUserResponseByUserUid(@RequestParam("userId") String userUid) {
        return new ResponseEntity<>(userAdminService.getUserResponseByUserUid(userUid), HttpStatus.OK);
    }

    @GetMapping
    @Operation(summary = "Fetch all users", description = "use to fetch the all users")
    public ResponseEntity<List<UserResponse>> fetchAllUsers(@RequestParam(required = false) String roleUid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        return new ResponseEntity<>(userAdminService.getAllUsers(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
    }

}
