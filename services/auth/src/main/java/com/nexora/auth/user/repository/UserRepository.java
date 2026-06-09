package com.nexora.auth.user.repository;

import com.nexora.auth.response.user.UserResponse;
import com.nexora.auth.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUid(String userUid);

    Optional<Users> findByUidAndEnabledTrue(String uid);

    Optional<Users> findByEmailAndEnabledTrue(String email);

    Optional<Users> findByEmail(String email);


}
