
package com.taniconnect.backend.dto;

import java.util.List;

public record LoginResponse(
        String accessToken,
        String tokenType,
        Long userId,
        String username,
        String email,
        List<String> roles
) {
}
