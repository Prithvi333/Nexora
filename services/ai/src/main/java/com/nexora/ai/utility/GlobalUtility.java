package com.nexora.ai.utility;

import com.nexora.ai.dto.UserPrinciple;
import org.springframework.security.core.context.SecurityContextHolder;

public class GlobalUtility {

    public static UserPrinciple getLoggedInUserDetails() {
        return (UserPrinciple) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
