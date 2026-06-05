package com.nexora.product.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateCategoryRequest(

        String categoryUid,

        @NotBlank(message = "Category name is required")
        @Size(min = 2, max = 100,
                message = "Category name must be between 2 and 100 characters")
        String name,

        String parentCategoryUid

) {
}