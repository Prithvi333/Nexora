package com.nexora.auth.user.service;

import com.nexora.auth.response.user.UserResponse;

import java.util.List;

public interface UserAdminService {

    UserResponse getUserResponseByUserUid(String uid);

    List<UserResponse> getAllUsers(Integer pageNo, Integer pageSize, String sortBy, String direction);
}
