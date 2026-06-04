package com.nexora.auth.user.service;

import com.nexora.auth.exception.token.IncorrectUserNameOrPasswordException;
import com.nexora.auth.exception.token.TokenException;
import com.nexora.auth.exception.users.EmptyUserList;
import com.nexora.auth.exception.users.PasswordException;
import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.request.token.CreateRefreshTokenRequest;
import com.nexora.auth.request.user.LoginRequest;
import com.nexora.auth.request.user.RegisterRequest;
import com.nexora.auth.request.user.UpdateUserRequest;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.token.TokenResponse;
import com.nexora.auth.response.user.RegisterResponse;
import com.nexora.auth.response.user.UserResponse;
import com.nexora.auth.role.model.Roles;
import com.nexora.auth.security.JwtService;
import com.nexora.auth.token.model.RefreshTokens;
import com.nexora.auth.token.repository.TokenRepository;
import com.nexora.auth.token.service.TokenService;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import com.nexora.auth.utils.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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
    private AuthenticationManager authenticationManager;

    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        Users user = convertToUserFromUserRequest(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        return convertToRegisterResponseFromUser(userRepository.save(user));
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
    public UserResponse getUserResponseByUserUid(String uid) {
        Optional<Users> user = userRepository.findByUidAndEnabledTrue(uid);
        if (user.isEmpty()) {
            throw new UserNotFound("User not found with uid " + uid + "");
        }
        return convertFromUserToUserResponse(user.get());

    }

    @Override
    public List<UserResponse> getAllUsers(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        sortBy = sortBy == null ? "username" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Users> page = userRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new EmptyUserList();
        }
        return page.getContent().stream().filter(Users::getEnabled).map(this::convertFromUserToUserResponse).toList();
    }

    @Override
    public TokenResponse userLogin(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        } catch (Exception ex) {
            throw new IncorrectUserNameOrPasswordException();
        }
        Map<String, Object> tokenMap = jwtService.generateToken(authentication);
        String refreshToken = generateRefreshToken(tokenMap.get("username"));
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

    private String generateRefreshToken(Object userName) {
        Optional<Users> user = userRepository.findByEmailAndEnabledTrue(userName.toString());
        if (user.isEmpty()) {
            throw new UserNotFound("User not found with username " + user + "");
        }
        CreateRefreshTokenRequest tokenRequest = CreateRefreshTokenRequest.builder().userUid(user.get().getUid()).expiryDate(LocalDate.now().plus(3, ChronoUnit.DAYS)).token(UUID.randomUUID().toString()).build();
        return tokenService.generateToken(tokenRequest).message();
    }

    private Users convertToUserFromUserRequest(RegisterRequest userRequest) {
        return Users.builder().uid(UUID.randomUUID().toString()).enabled(true)
                .refreshTokens(new ArrayList<>()).roles(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .username(userRequest.username()).email(userRequest.email())
                .password(userRequest.password()).build();
    }

    private RegisterResponse convertToRegisterResponseFromUser(Users user) {
        return RegisterResponse.builder().uid(user.getUid()).username(user.getUsername())
                .email(user.getEmail()).createdAt(user.getCreatedAt()).build();
    }

    private UserResponse convertFromUserToUserResponse(Users user) {
        return UserResponse.builder().uid(user.getUid()).username(user.getUsername()).email(user.getEmail())
                .roles(user.getRoles().stream().map(Roles::getRoleName)
                        .collect(Collectors.toSet())).createdAt(user.getCreatedAt()).build();
    }

}
