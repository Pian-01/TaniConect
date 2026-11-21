
package com.taniconnect.backend.controller;

import com.taniconnect.backend.dto.FollowResponse;
import com.taniconnect.backend.service.FollowService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{userId}/follow")
    public ResponseEntity<FollowResponse> followUser(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        FollowResponse response = followService.followUser(
                authentication.getName(), userId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<Void> unfollowUser(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        followService.unfollowUser(authentication.getName(), userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowResponse>> getFollowers(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                followService.getFollowers(userId)
        );
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowResponse>> getFollowing(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                followService.getFollowing(userId)
        );
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Map<String, Long>> getFollowersCount(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(Map.of(
                "followersCount",
                followService.getFollowersCount(userId)
        ));
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Map<String, Long>> getFollowingCount(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(Map.of(
                "followingCount",
                followService.getFollowingCount(userId)
        ));
    }
}
