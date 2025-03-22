package com.ecommerce.comment.service.impl;

import com.ecommerce.comment.dto.CommentDto;
import com.ecommerce.comment.entity.Comment;
import com.ecommerce.comment.exception.ResourceNotFoundException;
import com.ecommerce.comment.mapper.CommentMapper;
import com.ecommerce.comment.repository.CommentRepository;
import com.ecommerce.comment.service.CommentService;
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
    public List<CommentDto> getAllComments() {
        List<Comment> comments = commentRepository.findAll();
        return CommentMapper.INSTANCE.commentsToCommentDtos(comments);
    }

    @Override
    public CommentDto getCommentById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Comment", "id", id.toString())
        );
        return CommentMapper.INSTANCE.commentToCommentDto(comment);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, String token) {
        // Verfier l'existence de l'utilisateur
        if(!userApiClient.userExistsById(commentDto.getUserId(), token)) {
            throw new ResourceNotFoundException("User", "id", commentDto.getUserId().toString());
        }
        // Verifier l'existence du produit
        if(!productApiClient.productExistsById(commentDto.getProductId(), token)) {
            throw new ResourceNotFoundException("Product", "id", commentDto.getProductId().toString());
        }
        Comment comment = CommentMapper.INSTANCE.commentDtoToComment(commentDto);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.commentToCommentDto(savedComment);
    }

    @Override
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        getCommentById(id);
        Comment comment = CommentMapper.INSTANCE.commentDtoToComment(commentDto);
        comment.setId(id);
        Comment savedComment = commentRepository.save(comment);
        return CommentMapper.INSTANCE.commentToCommentDto(savedComment);
    }

    @Override
    public String deleteComment(Long id) {
        getCommentById(id);
        commentRepository.deleteById(id);
        return "Comment deleted successfully";
    }

    @Override
    public List<CommentDto> getCommentsByProductId(Long productId) {
        List<Comment> comments = commentRepository.findAllByProductId(productId);
        return CommentMapper.INSTANCE.commentsToCommentDtos(comments);
    }

    @Override
    public List<CommentDto> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentRepository.findAllByUserId(userId);
        return CommentMapper.INSTANCE.commentsToCommentDtos(comments);
    }
}
