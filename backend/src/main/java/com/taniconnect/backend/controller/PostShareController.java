
package com.taniconnect.backend.controller;

import com.taniconnect.backend.dto.PostShareResponse;
import com.taniconnect.backend.service.PostShareService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/share")
public class PostShareController {

    private final PostShareService postShareService;

    public PostShareController(PostShareService postShareService) {
        this.postShareService = postShareService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostShareResponse sharePost(
            @PathVariable Long postId,
            Authentication authentication) {
        return postShareService.sharePost(postId, authentication.getName());
    }

    @GetMapping("/count")
    public long getShareCount(@PathVariable Long postId) {
        return postShareService.getShareCount(postId);
    }
}
