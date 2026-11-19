
package com.taniconnect.backend.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record CommentResponse(
    Long id,
    Long postId,
    Long userId,
    String username,
    String content,
    Long parentCommentId,
    OffsetDateTime createdAt,
    List<CommentResponse> replies
) {}
