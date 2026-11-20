
package com.taniconnect.backend.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record PostResponse(
    Long id,
    Long userId,
    String username,
    String content,
    String postType,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    long likeCount,
    long commentCount,
    boolean isLikedByCurrentUser,
    List<PostMediaResponse> media
) {}
