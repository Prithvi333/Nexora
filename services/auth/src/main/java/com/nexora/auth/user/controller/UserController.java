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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(IUrls.USER)
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping(IUrls.USER_REGISTER)
    @Operation(summary = "User Creation", description = "use to create the user")
    public ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Received request to register user with email: {}", registerRequest.email());
        ResponseEntity<RegisterResponse> response = new ResponseEntity<>(userService.registerUser(registerRequest), HttpStatus.CREATED);
        logger.info("User registered successfully with email: {}", registerRequest.email());
        return response;
    }

    @PutMapping
    @Operation(summary = "Update User", description = "use to update the user")
    public ResponseEntity<SuccessResponse> updateUser(@Valid @RequestBody UpdateUserRequest userRequest) {
        logger.info("Received request to update user");
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(userService.updateUser(userRequest), HttpStatus.OK);
        logger.info("User updated successfully");
        return response;
    }

    @PostMapping(IUrls.USER_LOGIN)
    @Operation(summary = "User Login", description = "Login the user")
    public ResponseEntity<TokenResponse> userLogin(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Received login request for email: {}", loginRequest.email());
        ResponseEntity<TokenResponse> response = new ResponseEntity<>(userService.userLogin(loginRequest), HttpStatus.OK);
        logger.info("User logged in successfully with email: {}", loginRequest.email());
        return response;
    }

    @PostMapping(IUrls.USER_LOGOUT)
    @Operation(summary = "User Logout", description = "Logout the user")
    public ResponseEntity<SuccessResponse> userLogout(@RequestParam("refreshToken") String refreshToken) {
        logger.info("Received logout request");
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(userService.userLogout(refreshToken), HttpStatus.OK);
        logger.info("User logged out successfully");
        return response;
    }

}