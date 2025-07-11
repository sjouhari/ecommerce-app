package com.ecommerce.comment.controller;

import com.ecommerce.shared.dto.CommentDto;
import com.ecommerce.comment.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Validated
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllComments(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(commentService.getAllComments(token));
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByUserId(@PathVariable Long userId, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(commentService.getCommentsByUserId(userId, token));
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<List<CommentDto>> getAllCommentsByProductId(@PathVariable Long productId, @RequestHeader("Authorization") String token) {
            return ResponseEntity.ok(commentService.getCommentsByProductId(productId, token));
    }

    @GetMapping("{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(commentService.getCommentById(id, token));
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody @Valid CommentDto commentDto, @RequestHeader("Authorization") String token) {
        return new ResponseEntity<>(commentService.createComment(commentDto, token), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long id, @RequestBody @Valid CommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateComment(id, commentDto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/approve")
    public ResponseEntity<Void> approveComment(@PathVariable Long id) {
        commentService.approveComment(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{id}/reject")
    public ResponseEntity<Void> rejectComment(@PathVariable Long id) {
        commentService.rejectComment(id);
        return ResponseEntity.ok().build();
    }

}
