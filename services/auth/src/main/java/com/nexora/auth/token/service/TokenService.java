package com.nexora.auth.token.service;

import com.nexora.auth.request.token.CreateRefreshTokenRequest;
import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.token.RefreshTokenResponse;
import com.nexora.auth.token.model.RefreshTokens;
import com.nexora.auth.user.model.Users;

import java.util.List;

public interface TokenService {

    SuccessResponse generateToken(CreateRefreshTokenRequest tokenRequest);

//    SuccessResponse expireToken(String tokenUid);

    List<RefreshTokenResponse> findByUserUid(String userUid);


}
