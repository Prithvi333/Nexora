package com.nexora.auth.utils.contants;

import java.sql.Statement;

public interface IUrls {

//  Users endpoints

    public static final String USER = "/user";
    public static final String USER_LOGOUT = "/logout";
    public static final String USER_LOGIN = "/login";

// roles endpoints

    public static final String ROLE = "/role";
    public static final String UPDATE_USER_ROLE = "/assign";

//  token endpoints

    public static final String TOKEN = "/token";

}
