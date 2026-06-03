package com.nexora.auth.user.service;

import com.nexora.auth.request.user.RegisterRequest;
import com.nexora.auth.request.user.UpdateUserRequest;
import com.nexora.auth.response.user.RegisterResponse;
import com.nexora.auth.response.user.UserResponse;

import java.util.List;

public interface UserService {
    RegisterResponse registerUser(RegisterRequest registerRequest);

    String updateUser(UpdateUserRequest userRequest);

    UserResponse getUserResponseByUserUid(String uid);

    List<UserResponse> getAllUsers(Integer pageNo, Integer pageSize, String sortBy, String direction);


}
