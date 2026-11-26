package com.taniconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateReportStatusRequest(

        @NotBlank
        @Pattern(regexp = "REVIEWED|RESOLVED|REJECTED")
        String status

) {}