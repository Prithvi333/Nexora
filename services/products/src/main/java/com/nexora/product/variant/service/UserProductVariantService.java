package com.nexora.product.variant.service;

import com.nexora.product.response.variant.ProductVariantResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserProductVariantService {
    List<ProductVariantResponse> getProductVariant(String productVariantUid, Integer pageNo, Integer pageSize, String sortBy, String direction);

    void validateQuantity(String variantUid, Integer quantity);
}
