package com.nexora.user.preference.repository;

import com.nexora.user.preference.model.UserPreference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, String> {

    Page<UserPreference> findByUserUid(String uid, Pageable pageable);

    Optional<UserPreference> findByUidAndUserUid(String uid, String userUid);
}
