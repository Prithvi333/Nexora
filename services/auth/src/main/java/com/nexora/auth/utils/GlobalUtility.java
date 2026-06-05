package com.nexora.auth.utils;

import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.request.token.CreateRefreshTokenRequest;
import com.nexora.auth.request.user.RegisterRequest;
import com.nexora.auth.response.user.RegisterResponse;
import com.nexora.auth.response.user.UserResponse;
import com.nexora.auth.role.model.Roles;
import com.nexora.auth.user.model.Users;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class GlobalUtility {

    public static Pageable getPageable(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        pageSize = pageSize == null ? 5 : pageSize;
        pageNo = pageNo == null ? 0 : pageNo;
        Sort sort = direction == null ? Sort.by(sortBy).ascending() : direction.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        return PageRequest.of(pageNo, pageSize, sort);
    }



    public static Users convertToUserFromUserRequest(RegisterRequest userRequest) {
        return Users.builder().uid(UUID.randomUUID().toString()).enabled(true)
                .refreshTokens(new ArrayList<>()).roles(new HashSet<>())
                .createdAt(LocalDateTime.now())
                .username(userRequest.username()).email(userRequest.email())
                .password(userRequest.password()).build();
    }

    public static RegisterResponse convertToRegisterResponseFromUser(Users user) {
        return RegisterResponse.builder().uid(user.getUid()).username(user.getUsername())
                .email(user.getEmail()).createdAt(user.getCreatedAt()).build();
    }

    public static UserResponse convertFromUserToUserResponse(Users user) {
        return UserResponse.builder().uid(user.getUid()).username(user.getUsername()).email(user.getEmail())
                .roles(user.getRoles().stream().map(Roles::getRoleName)
                        .collect(Collectors.toSet())).createdAt(user.getCreatedAt()).build();
    }

}
