package com.nexora.auth.exception.token;

public class IncorrectUserNameOrPasswordException extends RuntimeException {
    public IncorrectUserNameOrPasswordException() {
        super("Incorrect password");
    }
}
