package com.ecommerce.comment.service.impl;

import com.ecommerce.comment.dto.CommentDto;
import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.mapper.CommentMapper;
import com.ecommerce.comment.repository.CommentRepository;
import com.ecommerce.comment.service.CommentService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductApiClient productApiClient;

    @Autowired
    private UserApiClient userApiClient;

    @Override
    public List<CommentDto> getAllComments(String token) {
        List<Comment> comments = commentRepository.findAll();
        List<CommentDto> commentDtos = CommentMapper.INSTANCE.commentsToCommentDtos(comments);
        return commentDtos.stream()
                .map(commentDto -> {
                    String username = userApiClient.getUserFullName(commentDto.getUserId(), token);
                    commentDto.setUsername(username);
                    return commentDto;
                }).toList();
    }

    @Override
    public CommentDto getCommentById(Long id, String token) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id.toString())
        );
        CommentDto commentDto = CommentMapper.INSTANCE.commentToCommentDto(comment);
        String username = userApiClient.getUserFullName(commentDto.getUserId(), token);
        commentDto.setUsername(username);
        return commentDto;
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, String token) {
        // Verfier l'existence de l'utilisateur
        String username = userApiClient.getUserFullName(commentDto.getUserId(), token);
        // Verifier l'existence du produit
        if(!productApiClient.productExistsById(commentDto.getProductId(), token)) {
            throw new ResourceNotFoundException("Product", "id", commentDto.getProductId().toString());
        }
        Comment comment = CommentMapper.INSTANCE.commentDtoToComment(commentDto);
        Comment savedComment = commentRepository.save(comment);
        CommentDto newCommentDto = CommentMapper.INSTANCE.commentToCommentDto(savedComment);
        newCommentDto.setUsername(username);
        return newCommentDto;
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id.toString())
        );
        Comment comment = CommentMapper.INSTANCE.commentDtoToComment(commentDto);
        comment.setId(id);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.commentToCommentDto(savedComment);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id.toString())
        );
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentDto> getCommentsByProductId(Long productId, String token) {
        List<Comment> comments = commentRepository.findAllByProductId(productId);
        List<CommentDto> commentDtos = CommentMapper.INSTANCE.commentsToCommentDtos(comments);
        return commentDtos.stream()
                .map(commentDto -> {
                    commentDto.setUsername(userApiClient.getUserFullName(commentDto.getUserId(), token));
                    return commentDto;
                }).toList();
    }

    @Override
    public List<CommentDto> getCommentsByUserId(Long userId, String token) {
        List<Comment> comments = commentRepository.findAllByUserId(userId);
        return CommentMapper.INSTANCE.commentsToCommentDtos(comments);
    }
}
