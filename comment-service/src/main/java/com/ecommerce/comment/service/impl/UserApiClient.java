package com.ecommerce.comment.service.impl;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "http://localhost:8080/api/users", name = "user-service")
@Headers("Authorization: {token}")
public interface UserApiClient {

    @GetMapping("/existsById/{id}")
    boolean userExistsById(@PathVariable Long id, @RequestHeader("Authorization") String token);
}
