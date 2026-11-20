
package com.taniconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdatePostRequest(

    @NotBlank(message = "Konten postingan wajib diisi")
    @Size(max = 10000, message = "Konten maksimal 10.000 karakter")
    String content,

    @NotBlank(message = "Jenis postingan wajib diisi")
    @Pattern(
        regexp = "THREAD|LISTING",
        message = "Jenis postingan harus THREAD atau LISTING"
    )
    String postType

) {}
