package com.nexora.auth.exception.users;

import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordException extends RuntimeException {
    public PasswordException(String message) {
        super(message);
    }
}
