
package com.taniconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePostMediaRequest(

    @NotBlank(message = "URL media wajib diisi")
    @Size(max = 2000, message = "URL media maksimal 2.000 karakter")
    String mediaUrl,

    @NotBlank(message = "Jenis media wajib diisi")
    @Pattern(
        regexp = "IMAGE|VIDEO",
        message = "Jenis media harus IMAGE atau VIDEO"
    )
    String mediaType,

    Integer displayOrder
) {}
