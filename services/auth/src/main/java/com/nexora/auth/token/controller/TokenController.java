package com.nexora.auth.token.controller;
import com.nexora.auth.response.token.RefreshTokenResponse;
import com.nexora.auth.token.service.TokenService;
import com.nexora.auth.utils.contants.IUrls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping(IUrls.ADMIN + IUrls.TOKEN)
public class TokenController {
    private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private TokenService tokenService;

    @GetMapping("{userUid}")
    public ResponseEntity<List<RefreshTokenResponse>> getAllRefreshTokenByUserUid(@RequestParam(required = false) Integer pageNo, @RequestParam(required = false) Integer pageSize, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String direction) {
        logger.info("Received request to fetch refresh tokens with pageNo: {}, pageSize: {}, sortBy: {}, direction: {}", pageNo, pageSize, sortBy, direction);
        ResponseEntity<List<RefreshTokenResponse>> response = new ResponseEntity<>(tokenService.findByUserUid(pageNo, pageSize, sortBy, direction), HttpStatus.OK);
        logger.info("Successfully fetched refresh tokens");
        return response;
    }

}