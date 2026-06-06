package com.nexora.product.variant.service;

import com.nexora.product.request.variant.ProductVariantRequest;
import com.nexora.product.request.variant.ProductVariantUpdateRequest;
import com.nexora.product.response.SuccessResponse;
import com.nexora.product.response.variant.ProductVariantResponse;

public interface ProductVariantService {

    ProductVariantResponse createProductVariant(ProductVariantRequest productVariantRequest);

    SuccessResponse updateProductVariant(ProductVariantUpdateRequest productVariantUpdateRequest);

    SuccessResponse deleteProductVariant(String productUid);

}
