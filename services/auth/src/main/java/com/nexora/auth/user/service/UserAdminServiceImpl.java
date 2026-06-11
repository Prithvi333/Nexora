package com.nexora.auth.user.service;

import com.nexora.auth.exception.users.EmptyUserList;
import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.kafka.enums.EventType;
import com.nexora.auth.kafka.producer.UserEventProducer;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.user.UserResponse;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import com.nexora.auth.utils.GlobalUtility;
import com.nexora.common.events.UserDeletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserAdminServiceImpl implements UserAdminService {

    private static final Logger log = LoggerFactory.getLogger(UserAdminServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserEventProducer userDeletedEventProducer;

    @Override
    public UserResponse getUserResponseByUserUid(String uid) {
        log.debug("Fetching user by uid: {}", uid);
        Optional<Users> user = userRepository.findByUidAndEnabledTrue(uid);
        if (user.isEmpty()) {
            log.warn("User not found with uid: {}", uid);
            throw new UserNotFound("User not found with uid " + uid + "");
        }
        log.info("User fetched successfully for uid: {}", uid);
        return GlobalUtility.convertFromUserToUserResponse(user.get());
    }

    @Override
    public SuccessResponse deleteUser(String usersUid) {
        log.debug("Attempting to delete user with uid: {}", usersUid);
        Users user = userRepository.findByUid(usersUid).orElseThrow(() -> {
            log.warn("Delete failed - user not found with uid: {}", usersUid);
            return new UserNotFound(usersUid);
        });
        userRepository.delete(user);
        userDeletedEventProducer.publishUserEvent(UserDeletedEvent.builder().userUid(usersUid).eventType(EventType.USER_DELETED).build());
        log.info("User deleted successfully with uid: {}", usersUid);
        return new SuccessResponse("User deleted successfully " + usersUid + "", HttpStatus.NO_CONTENT.value(), LocalDateTime.now());
    }

    @Override
    public List<UserResponse> getAllUsers(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        sortBy = sortBy == null ? "username" : sortBy;
        log.debug("Fetching all users - page: {}, size: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        Page<Users> page = userRepository.findAll(pageable);
        if (page.isEmpty()) {
            log.warn("No users found for page: {}, size: {}", pageNo, pageSize);
            throw new EmptyUserList();
        }
        List<UserResponse> users = page.getContent().stream().filter(Users::getEnabled).map(GlobalUtility::convertFromUserToUserResponse).toList();
        log.info("Returning {} users for page: {}", users.size(), pageNo);
        return users;
    }
}