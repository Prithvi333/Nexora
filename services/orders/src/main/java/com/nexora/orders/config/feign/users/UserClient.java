package com.nexora.orders.config.feign.users;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "users")
public interface UserClient {

    @GetMapping("/internal/user/profile/exists")
    Boolean isUserExists(@RequestParam("userProfileUid") String userProfileUid);

}
