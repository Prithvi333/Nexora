package com.nexora.gateway.utils;

import java.util.Arrays;
import java.util.List;

public interface IConstants {

    List<String> allowedUrls = Arrays.asList("/api/auth/user/login", "/api/auth/user/signup","/api/auth/user/token");
}
