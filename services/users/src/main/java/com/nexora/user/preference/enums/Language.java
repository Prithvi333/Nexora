package com.nexora.user.preference.enums;

public enum Language {

    HINDI,
    ENGLISH,
    FRENCH;

    public static boolean isExist(String lang) {

        if (lang == null) {
            return false;
        }

        for (Language language : values()) {
            if (language.name().equalsIgnoreCase(lang)) {
                return true;
            }
        }

        return false;
    }
}