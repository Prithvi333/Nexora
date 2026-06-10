package com.nexora.auth.token.controller;
import com.nexora.auth.response.token.RefreshTokenResponse;
import com.nexora.auth.token.service.TokenService;
import com.nexora.auth.utils.contants.IUrls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping(IUrls.USER + IUrls.TOKEN)
public class UserTokenController {
    private static final Logger logger = LoggerFactory.getLogger(UserTokenController.class);

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<RefreshTokenResponse> validateToken(@RequestHeader("Refresh-Token") String token) {
        logger.info("Received request to validate refresh token");
        ResponseEntity<RefreshTokenResponse> response = new ResponseEntity<>(tokenService.validateToken(token), HttpStatus.OK);
        logger.info("Refresh token validated successfully");
        return response;
    }

}