package com.nexora.auth.role.repository;

import com.nexora.auth.role.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByUid(String uid);
}
