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
import com.nexora.auth.utils.GlobalUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public SuccessResponse generateToken(CreateRefreshTokenRequest tokenRequest) {
        log.debug("Generating refresh token for userUid: {}", tokenRequest.userUid());
        RefreshTokens refreshToken = tokenRepository.save(convertFromTokenRequestToToken(tokenRequest));
        log.info("Refresh token generated and saved for userUid: {}", tokenRequest.userUid());
        return new SuccessResponse(refreshToken.getToken(), HttpStatus.OK.value(), LocalDateTime.now());
    }

    @Override
    public List<RefreshTokenResponse> findByUserUid(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        sortBy = sortBy == null ? "expiryDate" : sortBy;
        log.debug("Fetching refresh tokens - page: {}, size: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);
        Pageable pageable = GlobalUtility.getPageable(pageNo, pageSize, sortBy, direction);
        List<RefreshTokenResponse> tokens = tokenRepository.findAll(pageable).stream().map(this::convertFromRefreshTokenToRefreshTokenResponse).toList();
        log.info("Returning {} refresh token(s)", tokens.size());
        return tokens;
    }

    @Override
    public RefreshTokenResponse validateToken(String token) {
        log.debug("Validating token via JwtService");
        RefreshTokenResponse response = jwtService.validateToken(token);
        log.info("Token validated successfully");
        return response;
    }

    private RefreshTokens convertFromTokenRequestToToken(CreateRefreshTokenRequest tokenRequest) {
        log.debug("Looking up user for refresh token creation, userUid: {}", tokenRequest.userUid());
        Optional<Users> user = userRepository.findByUidAndEnabledTrue(tokenRequest.userUid());
        if (user.isEmpty()) {
            log.warn("Refresh token creation failed - user not found with uid: {}", tokenRequest.userUid());
            throw new UserNotFound("User not found with uid " + tokenRequest.userUid() + "");
        }
        log.debug("User found for uid: {}, building RefreshToken entity", tokenRequest.userUid());
        return RefreshTokens.builder().uid(UUID.randomUUID().toString()).token(tokenRequest.token()).expiryDate(tokenRequest.expiryDate()).user(user.get()).build();
    }

    private RefreshTokenResponse convertFromRefreshTokenToRefreshTokenResponse(RefreshTokens refreshTokens) {
        return RefreshTokenResponse.builder().refreshToken(refreshTokens.getToken()).expiryDate(refreshTokens.getExpiryDate().toString()).build();
    }
}