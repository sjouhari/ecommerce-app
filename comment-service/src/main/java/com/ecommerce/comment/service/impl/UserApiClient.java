package com.ecommerce.comment.service.impl;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("USER-SERVICE")
@Headers("Authorization: {token}")
public interface UserApiClient {

    @GetMapping("api/users/full-name/{id}")
    String getUserFullName(@PathVariable Long id, @RequestHeader("Authorization") String token);
}
