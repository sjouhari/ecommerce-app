package com.ecommerce.order.service.impl;

import com.ecommerce.order.dto.UserDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("USER-SERVICE")
@Headers("Authorization: {token}")
public interface UserApiClient {

    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable Long id, @RequestHeader("Authorization") String token);

}
