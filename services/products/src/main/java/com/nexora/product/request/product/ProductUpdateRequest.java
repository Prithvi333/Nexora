package com.nexora.product.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProductUpdateRequest(

        @NotBlank(message = "Product uid is required")
        @Size(max = 50)
        String uid,


        String name,

        @Size(max = 5000)
        String description,

        @Size(max = 100)
        String brand,

        Boolean active,

        String categoryUid
) {
}
