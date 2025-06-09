package com.ecommerce.product.service.impl;

import com.ecommerce.shared.dto.CommentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient("COMMENT-SERVICE")
public interface CommentApiClient {

    @GetMapping("/comments/product/{id}")
    List<CommentDto> getCommentsByProductId(@PathVariable Long id);

}
