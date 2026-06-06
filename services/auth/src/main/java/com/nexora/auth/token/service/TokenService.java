package com.nexora.auth.token.service;

import com.nexora.auth.request.token.CreateRefreshTokenRequest;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.token.RefreshTokenResponse;
import com.nexora.auth.response.token.TokenValidationResponse;

import java.util.List;

public interface TokenService {

    SuccessResponse generateToken(CreateRefreshTokenRequest tokenRequest);

    TokenValidationResponse validateToken(String token);

    List<RefreshTokenResponse> findByUserUid(String userUid);


}
