package com.nexora.auth.utils.contants;

import java.sql.Statement;

public interface IUrls {

//  Users endpoints

    String USER = "api/auth/user";
    String ADMIN = "api/auth/admin/user";
    String USER_REGISTER = "/signup";
    String USER_LOGOUT = "/logout";
    String USER_LOGIN = "/login";

// roles endpoints

    String ROLE = "api/auth/admin/role";
    String UPDATE_USER_ROLE = "/assign";

//  token endpoints

    String TOKEN = "api/auth/admin/token";

}
