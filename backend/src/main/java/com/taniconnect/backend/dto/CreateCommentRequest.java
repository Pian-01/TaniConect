
package com.taniconnect.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCommentRequest(

    @NotBlank(message = "Komentar tidak boleh kosong")
    @Size(max = 5000, message = "Komentar maksimal 5.000 karakter")
    String content,

    Long parentCommentId

) {}
