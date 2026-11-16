
package com.taniconnect.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Username wajib diisi")
        @Size(min = 3, max = 30, message = "Username harus 3-30 karakter")
        String username,

        @NotBlank(message = "Email wajib diisi")
        @Email(message = "Format email tidak valid")
        String email,

        @NotBlank(message = "Password wajib diisi")
        @Size(min = 8, message = "Password minimal 8 karakter")
        String password,

        @NotBlank(message = "Nama lengkap wajib diisi")
        @Size(max = 100, message = "Nama lengkap maksimal 100 karakter")
        String fullName,

        @Pattern(
                regexp = "^$|^[0-9+\\- ]{8,20}$",
                message = "Format nomor telepon tidak valid"
        )
        String phone

) {
}
