package com.ecommerce.comment.service;

import com.ecommerce.shared.dto.CommentDto;

import java.util.List;

public interface CommentService {

    List<CommentDto> getAllComments(String token);

    List<CommentDto> getAllApprovedComments(String token);

    CommentDto getCommentById(Long id, String token);

    CommentDto createComment(CommentDto commentDto, String token);

    CommentDto updateComment(Long id, CommentDto commentDto);

    void deleteComment(Long id);

    List<CommentDto> getCommentsByProductId(Long productId, String token);

    List<CommentDto> getCommentsByUserId(Long userId, String token);

    void approveComment(Long id);

    void rejectComment(Long id);

}
