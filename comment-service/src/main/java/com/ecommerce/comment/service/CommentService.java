package com.ecommerce.comment.service;

import com.ecommerce.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAllComments();

    CommentDto getCommentById(Long id);

    CommentDto createComment(CommentDto commentDto, String token);

    CommentDto updateComment(Long id, CommentDto commentDto);

    String deleteComment(Long id);

    List<CommentDto> getCommentsByProductId(Long productId);

    List<CommentDto> getCommentsByUserId(Long userId);

}
