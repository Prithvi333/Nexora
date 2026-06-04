package com.nexora.auth.token.controller;

import com.nexora.auth.response.token.RefreshTokenResponse;
import com.nexora.auth.token.service.TokenService;
import com.nexora.auth.utils.contants.IUrls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
