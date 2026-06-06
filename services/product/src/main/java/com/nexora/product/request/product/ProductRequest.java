package com.nexora.product.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductRequest(

        @NotBlank(message = "Product name is required")
        @Size(min = 2, max = 255)
        String name,

        @Size(max = 5000)
        String description,

        @NotBlank(message = "Brand is required")
        @Size(max = 100)
        String brand,

        Boolean active,

        @NotNull(message = "Category uid is required")
        String categoryUid

) {
}
