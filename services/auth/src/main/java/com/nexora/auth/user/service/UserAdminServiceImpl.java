package com.nexora.auth.user.service;

import com.nexora.auth.exception.users.EmptyUserList;
import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.response.user.UserResponse;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import com.nexora.auth.utils.GlobalUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class UserAdminServiceImpl implements UserAdminService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserResponse getUserResponseByUserUid(String uid) {
        Optional<Users> user = userRepository.findByUidAndEnabledTrue(uid);
        if (user.isEmpty()) {
            throw new UserNotFound("User not found with uid " + uid + "");
        }
        return GlobalUtility.convertFromUserToUserResponse(user.get());

    }

    @Override
    public List<UserResponse> getAllUsers(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        sortBy = sortBy == null ? "username" : sortBy;
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Users> page = userRepository.findAll(pageable);
        if (page.isEmpty()) {
            throw new EmptyUserList();
        }
        return page.getContent().stream().filter(Users::getEnabled).map(GlobalUtility::convertFromUserToUserResponse).toList();
    }
}
