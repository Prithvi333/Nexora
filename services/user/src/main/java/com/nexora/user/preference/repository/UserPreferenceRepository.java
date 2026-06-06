package com.nexora.user.preference.repository;

import com.nexora.user.preference.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, String> {

    Optional<UserPreference> findByUid(String uid);
}
