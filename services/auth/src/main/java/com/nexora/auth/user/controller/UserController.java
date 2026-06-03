package com.nexora.auth.user.controller;

import com.nexora.auth.request.user.RegisterRequest;
import com.nexora.auth.request.user.UpdateUserRequest;
import com.nexora.auth.response.user.RegisterResponse;
import com.nexora.auth.response.user.UserResponse;
import com.nexora.auth.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(userService.registerUser(registerRequest), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateUser(@Valid @RequestBody UpdateUserRequest userRequest) {
        return new ResponseEntity<>(userService.updateUser(userRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> fetchUserDetails(@RequestParam(required = false) String uid, @RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {

        if (uid != null) {
            return new ResponseEntity<>(List.of(userService.getUserResponseByUserUid(uid)), HttpStatus.OK);
        }
        return new ResponseEntity<>(userService.getAllUsers(pageNo, pageSize, sortBy, direction), HttpStatus.OK);

    }


}
