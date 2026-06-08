package com.nexora.user.profile.repository;

import com.nexora.user.profile.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {

    Optional<UserProfile> findByUid(String uid);

    boolean existsByUid(String uid);
}
