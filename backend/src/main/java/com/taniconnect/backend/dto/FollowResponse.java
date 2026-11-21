
package com.taniconnect.backend.dto;

import java.time.LocalDateTime;

public record FollowResponse(
    Long id,
    Long followerId,
    String followerUsername,
    Long followingId,
    String followingUsername,
    LocalDateTime createdAt
) {}
