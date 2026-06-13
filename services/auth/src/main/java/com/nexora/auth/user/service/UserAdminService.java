package com.nexora.auth.user.service;

import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.user.UserResponse;

import java.util.List;

public interface UserAdminService {

    UserResponse getUserResponseByUserUid(String uid);

    SuccessResponse deleteUser(String usersUid);

    List<UserResponse> getAllUsers(Integer pageNo, Integer pageSize, String sortBy, String direction);
}
