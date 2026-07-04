package com.nexora.user.exception.preference;

public class LanguageNotFound extends RuntimeException {
    public LanguageNotFound(String language) {
        super("Language not found with name " + language + "");
    }
}
