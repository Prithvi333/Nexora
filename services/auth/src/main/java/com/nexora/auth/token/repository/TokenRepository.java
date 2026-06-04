package com.nexora.auth.token.repository;

import com.nexora.auth.token.model.RefreshTokens;
import com.nexora.auth.user.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshTokens, Long> {
    @Query("""
            SELECT r
            FROM RefreshTokens r
            WHERE r.user.uid = :userUid
            """)
    List<RefreshTokens> findByUserUid(@Param("userUid") String userUid);

    Optional<RefreshTokens> findByToken(String refreshToken);


}
