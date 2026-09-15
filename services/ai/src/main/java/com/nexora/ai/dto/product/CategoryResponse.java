package com.nexora.ai.dto.product;

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
