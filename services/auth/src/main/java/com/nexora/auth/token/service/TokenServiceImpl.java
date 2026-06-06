package com.nexora.auth.token.service;

import com.nexora.auth.exception.users.UserNotFound;
import com.nexora.auth.request.token.CreateRefreshTokenRequest;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.token.RefreshTokenResponse;
import com.nexora.auth.response.token.TokenValidationResponse;
import com.nexora.auth.security.JwtService;
import com.nexora.auth.token.model.RefreshTokens;
import com.nexora.auth.token.repository.TokenRepository;
import com.nexora.auth.user.model.Users;
import com.nexora.auth.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public SuccessResponse generateToken(CreateRefreshTokenRequest tokenRequest) {
        RefreshTokens refreshToken = tokenRepository.save(convertFromTokenRequestToToken(tokenRequest));
        return new SuccessResponse(refreshToken.getToken(), HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public List<RefreshTokenResponse> findByUserUid(String userUid) {
        return tokenRepository.findByUserUid(userUid).stream().map(this::convertFromRefreshTokenToRefreshTokenResponse).toList();
    }

    @Override
    public TokenValidationResponse validateToken(String token) {
        return jwtService.validateToken(token);
    }

    private RefreshTokens convertFromTokenRequestToToken(CreateRefreshTokenRequest tokenRequest) {
        Optional<Users> user = userRepository.findByUidAndEnabledTrue(tokenRequest.userUid());
        if (user.isEmpty()) {
            throw new UserNotFound("User not found with uid " + tokenRequest.userUid() + "");
        }
        return RefreshTokens.builder().uid(UUID.randomUUID().toString()).token(tokenRequest.token()).expiryDate(tokenRequest.expiryDate()).user(user.get()).build();
    }

    private RefreshTokenResponse convertFromRefreshTokenToRefreshTokenResponse(RefreshTokens refreshTokens) {
        return RefreshTokenResponse.builder().refreshToken(refreshTokens.getToken()).expiryDate(refreshTokens.getExpiryDate().toString()).build();
    }
}
