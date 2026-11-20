
package com.taniconnect.backend.dto;

import java.time.LocalDateTime;

public record PostMediaResponse(
    Long id,
    Long postId,
    String mediaUrl,
    String mediaType,
    Integer displayOrder,
    LocalDateTime createdAt
) {}
