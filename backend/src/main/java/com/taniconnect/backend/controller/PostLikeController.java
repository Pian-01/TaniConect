package com.taniconnect.backend.controller;

import com.taniconnect.backend.service.PostLikeService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostLikeController {

    private final PostLikeService postLikeService;

    public PostLikeController(PostLikeService postLikeService) {
        this.postLikeService = postLikeService;
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<String> likePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        String result = postLikeService.likePost(
                postId,
                authentication.getName()
        );

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<String> unlikePost(
            @PathVariable Long postId,
            Authentication authentication
    ) {
        String result = postLikeService.unlikePost(
                postId,
                authentication.getName()
        );

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{postId}/likes")
    public ResponseEntity<Long> countLikes(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(
                postLikeService.countLikes(postId)
        );
    }
}