package com.taniconnect.backend.dto;

public record UserProfileResponse(
        Long id,
        String username,
        String email,
        String fullName,
        String phone,
        String profilePicture,
        String bio,
        String status
) {}