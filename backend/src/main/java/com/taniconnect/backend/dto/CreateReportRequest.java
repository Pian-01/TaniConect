
package com.taniconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateReportRequest(

    @NotBlank
    @Pattern(regexp = "POST|COMMENT|ACCOUNT")
    String targetType,

    Long targetId,

    @NotBlank
    @Size(max = 255)
    String reason,

    @Size(max = 5000)
    String description
) {}
