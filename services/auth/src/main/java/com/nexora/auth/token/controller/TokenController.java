package com.nexora.auth.token.controller;

import com.nexora.auth.response.SuccessResponse;
import com.nexora.auth.response.token.RefreshTokenResponse;
import com.nexora.auth.response.token.TokenValidationResponse;
import com.nexora.auth.token.service.TokenService;
import com.nexora.auth.utils.contants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(IUrls.TOKEN)
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("{userUid}")
    public ResponseEntity<List<RefreshTokenResponse>> getAllRefreshTokenByUserUid(@PathVariable("userUid") String userUid) {
        return new ResponseEntity<>(tokenService.findByUserUid(userUid), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(tokenService.validateToken(token), HttpStatus.OK);
    }

}
