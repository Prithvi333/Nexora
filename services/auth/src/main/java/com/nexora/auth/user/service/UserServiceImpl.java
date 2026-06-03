package com.nexora.auth.user.service;

import com.nexora.auth.exception.users.EmptyUserList;
import com.nexora.auth.exception.users.PasswordException;
import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.request.user.RegisterRequest;
import com.nexora.auth.request.user.UpdateUserRequest;
import com.nexora.auth.response.user.RegisterResponse;
import com.nexora.auth.response.user.UserResponse;
import com.nexora.auth.role.model.Roles;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import com.nexora.auth.utils.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        Users user = convertToUserFromUserRequest(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        return convertToRegisterResponseFromUser(userRepository.save(user));
    }

    @Override
    public String updateUser(UpdateUserRequest userRequest) {

        Optional<Users> user = userRepository.findByEmailAndEnabledTrue(userRequest.email());
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
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Users> page = userRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new EmptyUserList();
        }
        return page.getContent().stream().filter(Users::getEnabled).map(this::convertFromUserToUserResponse).toList();
    }

    private Users convertToUserFromUserRequest(RegisterRequest userRequest) {
        return Users.builder().uid(UUID.randomUUID().toString()).enabled(true)
                .refreshTokens(new ArrayList<>()).roles(new HashSet<>())
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
