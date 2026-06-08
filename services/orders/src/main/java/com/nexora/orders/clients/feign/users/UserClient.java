package com.nexora.orders.clients.feign.users;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "users")
public interface UserClient {

    @GetMapping("/internal/user/profile/exists")
    void isUserExists(String userUid);

}
