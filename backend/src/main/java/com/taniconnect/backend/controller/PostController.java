
package com.taniconnect.backend.controller;

import com.taniconnect.backend.dto.CreatePostRequest;
import com.taniconnect.backend.dto.PostResponse;
import com.taniconnect.backend.service.PostService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            Authentication authentication,
            @Valid @RequestBody CreatePostRequest request
    ) {
        PostResponse response = postService.createPost(
                authentication.getName(),
                request
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    @GetMapping("/me")
    public ResponseEntity<List<PostResponse>> getMyPosts(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                postService.getMyPosts(authentication.getName())
        );
    }
}
