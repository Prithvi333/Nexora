package com.nexora.product.response.category;

import lombok.Builder;

import java.util.List;

@Builder
public record CategoryResponse(

        String uid,

        String name,

        String parentCategoryUid,

        String parentCategoryName,

        List<String> productUids
) {
}
