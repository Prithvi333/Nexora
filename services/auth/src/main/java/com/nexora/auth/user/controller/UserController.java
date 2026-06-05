package com.nexora.auth.user.controller;

import com.nexora.auth.request.user.LoginRequest;
import com.nexora.auth.request.user.RegisterRequest;
import com.nexora.auth.request.user.UpdateUserRequest;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.token.TokenResponse;
import com.nexora.auth.response.user.RegisterResponse;
import com.nexora.auth.user.service.UserService;
import com.nexora.auth.utils.contants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(IUrls.USER)
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "User Creation", description = "use to create the user")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        return new ResponseEntity<>(userService.registerUser(registerRequest), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "Update User", description = "use to update the user")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UpdateUserRequest userRequest) {
        return new ResponseEntity<>(userService.updateUser(userRequest), HttpStatus.OK);
    }

    @PostMapping(IUrls.USER_LOGIN)
    @Operation(summary = "User Login", description = "Login the user")
    public ResponseEntity<TokenResponse> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.userLogin(loginRequest), HttpStatus.OK);
    }

    @PostMapping(IUrls.USER_LOGOUT)
    @Operation(summary = "User Logout", description = "Logout the user")
    public ResponseEntity<SuccessResponse> userLogout(@RequestParam("refreshToken") String refreshToken) {
        return new ResponseEntity<>(userService.userLogout(refreshToken), HttpStatus.OK);
    }


}
