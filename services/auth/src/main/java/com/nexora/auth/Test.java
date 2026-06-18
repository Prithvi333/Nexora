package com.nexora.auth;

import com.nexora.auth.request.user.RegisterRequest;
import com.nexora.auth.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Test implements CommandLineRunner {

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {
       userService.registerUser(new RegisterRequest("prithvi", "pthakur.pt36@gmail.com", "prithvi121"));
    }
}
