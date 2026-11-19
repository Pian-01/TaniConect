
package com.taniconnect.backend.controller;

import com.taniconnect.backend.dto.CommentResponse;
import com.taniconnect.backend.dto.UpdateCommentRequest;
import com.taniconnect.backend.dto.CreateCommentRequest;
import com.taniconnect.backend.service.PostCommentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class PostCommentController {

    private final PostCommentService commentService;

    public PostCommentController(PostCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            Authentication authentication,
            @Valid @RequestBody CreateCommentRequest request
    ) {
        CommentResponse response = commentService.createComment(
                postId,
                authentication.getName(),
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(commentService.getComments(postId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication
    ) {
        commentService.deleteComment(
                commentId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            Authentication authentication,
            @Valid @RequestBody UpdateCommentRequest request
    ) {
        CommentResponse response = commentService.updateComment(
                commentId,
                authentication.getName(),
                request
    );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countComments(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(
            commentService.countComments(postId)
        );
    }
}
