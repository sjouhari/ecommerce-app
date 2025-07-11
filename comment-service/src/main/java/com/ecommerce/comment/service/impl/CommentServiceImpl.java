package com.ecommerce.comment.service.impl;

import com.ecommerce.shared.dto.CommentDto;
import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.mapper.CommentMapper;
import com.ecommerce.comment.repository.CommentRepository;
import com.ecommerce.comment.service.CommentService;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProductApiClient productApiClient;
    private final UserApiClient userApiClient;

    public CommentServiceImpl(CommentRepository commentRepository, ProductApiClient productApiClient, UserApiClient userApiClient) {
        this.commentRepository = commentRepository;
        this.productApiClient = productApiClient;
        this.userApiClient = userApiClient;
    }

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
    public List<CommentDto> getAllApprovedComments(String token) {
        return commentRepository.findAll().stream()
                .filter(Comment::isApproved)
                .map(comment -> {
                    CommentDto commentDto = CommentMapper.INSTANCE.commentToCommentDto(comment);
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
        comment.setApproved(false);
        comment.setRejected(false);
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
                .filter(CommentDto::isApproved)
                .map(commentDto -> {
                    commentDto.setUsername(userApiClient.getUserFullName(commentDto.getUserId(), token));
                    return commentDto;
                }).toList();
    }

    @Override
    public List<CommentDto> getCommentsByUserId(Long userId, String token) {
        List<Comment> comments = commentRepository.findAllByUserId(userId).stream()
                .filter(Comment::isApproved)
                .toList();
        return CommentMapper.INSTANCE.commentsToCommentDtos(comments);
    }

    @Override
    public void approveComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id.toString())
        );
        comment.setApproved(true);
        comment.setRejected(false);
        commentRepository.save(comment);
    }

    @Override
    public void rejectComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id.toString())
        );
        comment.setApproved(false);
        comment.setRejected(true);
        commentRepository.save(comment);
    }
}
