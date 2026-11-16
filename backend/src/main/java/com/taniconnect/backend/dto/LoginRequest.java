
package com.taniconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @NotBlank(message = "Username atau email wajib diisi")
        String usernameOrEmail,

        @NotBlank(message = "Password wajib diisi")
        String password

) {
}
