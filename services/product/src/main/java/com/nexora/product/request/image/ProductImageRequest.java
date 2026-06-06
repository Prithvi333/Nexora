package com.nexora.product.request.image;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ProductImageRequest(

        Boolean primary,

        MultipartFile multipartFile,

        @NotNull(message = "product variant uid is required")
        String productVariantUid

) {
}
