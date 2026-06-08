package com.nexora.auth.user.service;

import com.nexora.auth.exception.token.IncorrectUserNameOrPasswordException;
import com.nexora.auth.exception.token.TokenException;
import com.nexora.auth.exception.users.PasswordException;
import com.nexora.auth.exception.users.UserNotFound;
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
import jakarta.transaction.Transactional;
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
    private AuthenticationManager authenticationManager;

    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        Users user = GlobalUtility.convertToUserFromUserRequest(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setRoles(Set.of(fetchDefaultRole()));
        return GlobalUtility.convertToRegisterResponseFromUser(userRepository.save(user));
    }

    private Roles fetchDefaultRole() {
        try {
            return roleRepository
                    .findByRoleName("ROLE_" + IRole.ROLE_USER)
                    .orElseThrow(() ->
                            new RoleNotFoundException("User role not found"));

        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String updateUser(UpdateUserRequest userRequest) {

        Optional<Users> user = userRepository.findByEmail(userRequest.email());
        StringBuilder sb = new StringBuilder();

        if (user.isEmpty()) {
            throw new UserNotFound("User not found with email " + userRequest.email() + "");
        }
        Users currentUser = user.get();

        if (userRequest.currentPassword() != null && userRequest.newPassword() != null) {

            if (userRequest.newPassword().length() < 8) {
                throw new PasswordException("Password must contains at least 8 characters");
            }
            if (!passwordEncoder.matches(userRequest.currentPassword(), currentUser.getPassword())) {
                throw new PasswordException("Old password does not matches");
            }

            currentUser.setPassword(passwordEncoder.encode(userRequest.newPassword()));
            sb.append("Password ");
        }
        if (userRequest.username() != null && !userRequest.username().isBlank() && !userRequest.username().equals(currentUser.getUsername())) {
            currentUser.setUsername(userRequest.username());
            sb.append("Username ");
        }
        if (userRequest.enabled() != null && userRequest.enabled() != currentUser.getEnabled()) {
            currentUser.setEnabled(userRequest.enabled());
            sb.append("Enabled ");
        }

        userRepository.save(currentUser);

        return sb.isEmpty() ? "Nothing to update" : sb.toString() + "updated successfully";

    }

    @Override
    public TokenResponse userLogin(LoginRequest loginRequest) {
        Authentication authentication;
        Users user = userRepository.findByEmail(loginRequest.email()).orElseThrow(UserNotFound::new);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        } catch (Exception ex) {
            throw new IncorrectUserNameOrPasswordException();
        }
        Map<String, Object> tokenMap = jwtService.generateToken(user);
        String userName = tokenMap.get("username").toString();
        boolean isEmptyRefreshToken = user.getRefreshTokens().isEmpty();
        boolean isRefreshTokenExpired = user.getRefreshTokens().stream().noneMatch(token -> (token.getExpiryDate().isAfter(LocalDateTime.now()) || (token.getExpiryDate().isEqual(LocalDateTime.now()))));
        String refreshToken = "";
        if (isEmptyRefreshToken || isRefreshTokenExpired) {
            refreshToken = generateRefreshToken(userName);
        }
        return TokenResponse.builder().refreshToken(refreshToken).accessToken(tokenMap.get("accessToken").toString()).build();
    }

    @Override
    public SuccessResponse userLogout(String refreshToken) {
        Optional<RefreshTokens> refreshTokens = tokenRepository.findByToken(refreshToken);
        if (refreshTokens.isEmpty()) {
            throw new TokenException("Refresh token not found");
        }
        RefreshTokens currentRefreshToken = refreshTokens.get();
        tokenRepository.delete(currentRefreshToken);

        return new SuccessResponse("Logout successfully", HttpStatus.OK.value(), LocalDateTime.now());
    }

    private String generateRefreshToken(String userName) {
        Optional<Users> user = userRepository.findByEmailAndEnabledTrue(userName);
        if (user.isEmpty()) {
            throw new UserNotFound("User not found with username " + user + "");
        }
        CreateRefreshTokenRequest tokenRequest = CreateRefreshTokenRequest.builder().userUid(user.get().getUid()).expiryDate(LocalDateTime.now().plus(2, ChronoUnit.HOURS)).token(UUID.randomUUID().toString()).build();
        return tokenService.generateToken(tokenRequest).message();
    }

}
