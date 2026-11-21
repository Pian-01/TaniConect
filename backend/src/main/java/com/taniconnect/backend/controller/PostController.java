
package com.taniconnect.backend.controller;

import com.taniconnect.backend.dto.CreatePostRequest;
import com.taniconnect.backend.dto.PostResponse;
import com.taniconnect.backend.dto.UpdatePostRequest;
import com.taniconnect.backend.service.PostService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

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
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                postService.getAllPosts(
                        authentication.getName(),
                        page,
                        size
                )
        );
    }


        @GetMapping("/following")
        public ResponseEntity<Page<PostResponse>> getFollowingPosts(
                Authentication authentication,
                @RequestParam(defaultValue = "0") int page,
                @RequestParam(defaultValue = "10") int size
        ) {
        return ResponseEntity.ok(
                postService.getFollowingPosts(
                            authentication.getName(),
                            page,
                            size
                    )
                );
        }


    @GetMapping("/me")
    public ResponseEntity<List<PostResponse>> getMyPosts(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                postService.getMyPosts(authentication.getName())
        );
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdatePostRequest request
    ) {
        PostResponse response = postService.updatePost(
                postId,
                userDetails.getUsername(),
                request
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        postService.deletePost(postId, userDetails.getUsername());

        return ResponseEntity.noContent().build();
    }
}
