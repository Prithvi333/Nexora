package com.nexora.auth.user.service;

import com.nexora.auth.exception.token.IncorrectUserNameOrPasswordException;
import com.nexora.auth.exception.token.TokenException;
import com.nexora.auth.exception.users.PasswordException;
import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.kafka.enums.EventType;
import com.nexora.auth.kafka.producer.UserEventProducer;
import com.nexora.auth.request.token.CreateRefreshTokenRequest;
import com.nexora.auth.request.user.LoginRequest;
import com.nexora.auth.request.user.RegisterRequest;
import com.nexora.auth.request.user.UpdateUserRequest;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.token.TokenResponse;
import com.nexora.auth.response.user.RegisterResponse;
import com.nexora.auth.role.model.Roles;
import com.nexora.auth.role.repository.RoleRepository;
import com.nexora.auth.security.JwtService;
import com.nexora.auth.token.model.RefreshTokens;
import com.nexora.auth.token.repository.TokenRepository;
import com.nexora.auth.token.service.TokenService;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import com.nexora.auth.utils.GlobalUtility;
import com.nexora.auth.utils.contants.IRole;
import com.nexora.common.events.UserCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserEventProducer userCreatedEventProducer;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        log.debug("Registering new user with email: {}", registerRequest.email());
        Users user = GlobalUtility.convertToUserFromUserRequest(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRoles(Set.of(fetchDefaultRole()));
        Users createdUser = userRepository.save(user);
        log.info("User registered successfully with uid: {}", createdUser.getUid());
        UserCreatedEvent userCreatedEvent = UserCreatedEvent.builder().email(createdUser.getEmail()).userUid(createdUser.getUid()).username(createdUser.getUsername()).eventType(EventType.USER_CREATED).build();
        userCreatedEventProducer.publishUserEvent(userCreatedEvent);
        log.debug("UserCreatedEvent published for uid: {}", createdUser.getUid());
        return GlobalUtility.convertToRegisterResponseFromUser(createdUser);
    }

    private Roles fetchDefaultRole() {
        log.debug("Fetching default role: ROLE_{}", IRole.ROLE_USER);
        try {
            return roleRepository
                    .findByRoleName("ROLE_" + IRole.ROLE_USER)
                    .orElseThrow(() ->
                            new RoleNotFoundException("User role not found"));

        } catch (RoleNotFoundException e) {
            log.error("Default role ROLE_{} not found in database", IRole.ROLE_USER);
            throw new RuntimeException(e);
        }
    }

    @Override
    public SuccessResponse updateUser(UpdateUserRequest userRequest) {
        log.debug("Attempting to update user with email: {}", userRequest.email());
        Optional<Users> user = userRepository.findByEmail(userRequest.email());

        if (user.isEmpty()) {
            log.warn("Update failed - user not found with email: {}", userRequest.email());
            throw new UserNotFound("User not found with email " + userRequest.email() + "");
        }
        Users currentUser = user.get();

        if (userRequest.currentPassword() != null && userRequest.newPassword() != null) {

            if (userRequest.newPassword().length() < 8) {
                log.warn("Password update failed for uid: {} - new password too short", currentUser.getUid());
                throw new PasswordException("Password must contains at least 8 characters");
            }
            if (!passwordEncoder.matches(userRequest.currentPassword(), currentUser.getPassword())) {
                log.warn("Password update failed for uid: {} - current password mismatch", currentUser.getUid());
                throw new PasswordException("Old password does not matches");
            }

            currentUser.setPassword(passwordEncoder.encode(userRequest.newPassword()));
        }
        if (userRequest.username() != null && !userRequest.username().isBlank() && !userRequest.username().equals(currentUser.getUsername())) {
            currentUser.setUsername(userRequest.username());
        }
        if (userRequest.enabled() != null && userRequest.enabled() != currentUser.getEnabled()) {
            currentUser.setEnabled(userRequest.enabled());
        }

        userRepository.save(currentUser);
        return new SuccessResponse("User has been updated successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public TokenResponse userLogin(LoginRequest loginRequest) {
        log.debug("Login attempt for email: {}", loginRequest.email());
        Authentication authentication;
        Users user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> {
            log.warn("Login failed - user not found with email: {}", loginRequest.email());
            return new UserNotFound();
        });
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        } catch (Exception ex) {
            log.warn("Login failed - incorrect credentials for email: {}", loginRequest.email());
            throw new IncorrectUserNameOrPasswordException();
        }
        Map<String, Object> tokenMap = jwtService.generateToken(user);
        String userName = tokenMap.get("username").toString();
        boolean isEmptyRefreshToken = user.getRefreshTokens().isEmpty();
        boolean isRefreshTokenExpired = user.getRefreshTokens().stream().noneMatch(token -> (token.getExpiryDate().isAfter(LocalDateTime.now()) || (token.getExpiryDate().isEqual(LocalDateTime.now()))));
        String refreshToken = "";
        if (isEmptyRefreshToken || isRefreshTokenExpired) {
            log.debug("No valid refresh token found for uid: {}, generating new one", user.getUid());
            refreshToken = generateRefreshToken(userName);
        }
        log.info("User logged in successfully for uid: {}", user.getUid());
        return TokenResponse.builder().refreshToken(refreshToken).accessToken(tokenMap.get("accessToken").toString()).build();
    }

    @Override
    public SuccessResponse userLogout(String refreshToken) {
        log.debug("Logout attempt with refresh token");
        Optional<RefreshTokens> refreshTokens = tokenRepository.findByToken(refreshToken);
        if (refreshTokens.isEmpty()) {
            log.warn("Logout failed - refresh token not found");
            throw new TokenException("Refresh token not found");
        }
        RefreshTokens currentRefreshToken = refreshTokens.get();
        tokenRepository.delete(currentRefreshToken);
        log.info("User logged out successfully, refresh token deleted");
        return new SuccessResponse("Logout successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    private String generateRefreshToken(String userName) {
        log.debug("Generating refresh token for username: {}", userName);
        Optional<Users> user = userRepository.findByEmailAndEnabledTrue(userName);
        if (user.isEmpty()) {
            log.warn("Refresh token generation failed - user not found with username: {}", userName);
            throw new UserNotFound("User not found with username " + user + "");
        }
        CreateRefreshTokenRequest tokenRequest = CreateRefreshTokenRequest.builder().userUid(user.get().getUid()).expiryDate(LocalDateTime.now().plus(2, ChronoUnit.HOURS)).token(UUID.randomUUID().toString()).build();
        log.info("Refresh token generated for uid: {}", user.get().getUid());
        return tokenService.generateToken(tokenRequest).message();
    }

}