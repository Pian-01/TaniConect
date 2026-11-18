package com.taniconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @NotBlank(message = "Nama lengkap wajib diisi")
        @Size(max = 100, message = "Nama maksimal 100 karakter")
        String fullName,

        @Size(max = 20, message = "Nomor telepon maksimal 20 karakter")
        String phone,

        @Size(max = 500, message = "Bio maksimal 500 karakter")
        String bio,

        String profilePicture
) {}