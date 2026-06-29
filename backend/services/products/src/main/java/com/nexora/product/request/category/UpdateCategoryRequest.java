package com.nexora.product.request.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateCategoryRequest(

        String categoryUid,

        String name,

        String parentCategoryUid

) {
}