
package com.taniconnect.backend.dto;

import java.time.LocalDateTime;

public record ReportResponse(
    Long id,
    Long reporterId,
    String targetType,
    Long targetId,
    String reason,
    String description,
    String status,
    LocalDateTime createdAt
) {}
