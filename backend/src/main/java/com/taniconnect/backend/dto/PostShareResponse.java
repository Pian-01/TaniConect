
package com.taniconnect.backend.dto;

import java.time.LocalDateTime;

public record PostShareResponse(
    Long id,
    Long postId,
    Long userId,
    String username,
    LocalDateTime createdAt
) {}
