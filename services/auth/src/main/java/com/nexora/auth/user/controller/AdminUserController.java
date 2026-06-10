package com.nexora.auth.user.controller;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.user.UserResponse;
import com.nexora.auth.user.service.UserAdminService;
import com.nexora.auth.utils.contants.IUrls;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(IUrls.ADMIN)
public class AdminUserController {
    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserAdminService userAdminService;

    @PostMapping
    @Operation(summary = "Fetch user", description = "use to fetch the user based on user Uid")
    public ResponseEntity<UserResponse> getUserResponseByUserUid(@RequestParam("userId") String userUid) {
        logger.info("Received request to fetch user with userUid: {}", userUid);
        ResponseEntity<UserResponse> response = new ResponseEntity<>(userAdminService.getUserResponseByUserUid(userUid), HttpStatus.OK);
        logger.info("Successfully fetched user with userUid: {}", userUid);
        return response;
    }

    @DeleteMapping
    @Operation(summary = "Delete user", description = "use to delete the user")
    public ResponseEntity<SuccessResponse> deleteUser(@RequestParam("userId") String userUid) {
        logger.info("Received request to delete user with userUid: {}", userUid);
        ResponseEntity<SuccessResponse> response = new ResponseEntity<>(userAdminService.deleteUser(userUid), HttpStatus.NO_CONTENT);
        logger.info("Successfully deleted user with userUid: {}", userUid);
        return response;
    }

    @GetMapping
    @Operation(summary = "Fetch all users", description = "use to fetch the all users")
    public ResponseEntity<List<UserResponse>> fetchAllUsers(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        logger.info("Received request to fetch all users with pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);
        ResponseEntity<List<UserResponse>> response = new ResponseEntity<>(userAdminService.getAllUsers(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("Successfully fetched all users");
        return response;
    }

}