package com.ecommerce.comment.repository;

import com.ecommerce.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByProductId(Long productId);

    List<Comment> findAllByUserId(Long userId);

}
