
package com.taniconnect.backend.dto;

public record RegisterResponse(
        Long id,
        String username,
        String email,
        String fullName,
        String status
) {
}
