
package com.taniconnect.backend.controller;

import com.taniconnect.backend.dto.CreatePostMediaRequest;
import com.taniconnect.backend.dto.PostMediaResponse;
import com.taniconnect.backend.service.PostMediaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/media")
public class PostMediaController {

    private final PostMediaService postMediaService;

    public PostMediaController(PostMediaService postMediaService) {
        this.postMediaService = postMediaService;
    }

    @PostMapping
    public ResponseEntity<PostMediaResponse> addMedia(
            @PathVariable Long postId,
            Authentication authentication,
            @Valid @RequestBody CreatePostMediaRequest request
    ) {
        PostMediaResponse response = postMediaService.addMedia(
                postId,
                authentication.getName(),
                request
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PostMediaResponse>> getPostMedia(
            @PathVariable Long postId
    ) {
        return ResponseEntity.ok(
                postMediaService.getPostMedia(postId)
        );
    }

    @DeleteMapping("/{mediaId}")
    public ResponseEntity<Void> deleteMedia(
            @PathVariable Long postId,
            @PathVariable Long mediaId,
            Authentication authentication
    ) {
        postMediaService.deleteMedia(
                mediaId,
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }
}
